package com.alfredo.geoeventosapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagenServiceTest {

    // ── Cadena real de WebClient ─────────────────────────────────────────────
    // post() → RequestBodyUriSpec
    // .uri() → RequestBodySpec
    // .body() → RequestHeadersSpec
    // .retrieve() → ResponseSpec
    // .bodyToMono() → Mono
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private WebClient.RequestBodySpec requestBodySpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private ImagenService service;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        service = new ImagenService(webClient);

        // Inyectamos los @Value manualmente sin levantar contexto Spring
        ReflectionTestUtils.setField(service, "apiKey", "fake-api-key");
        ReflectionTestUtils.setField(service, "apiUrl", "https://api.imgbb.com/1/upload");

        // Encadenamos los mocks siguiendo la cadena real de WebClient
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);  // body() → RequestHeadersSpec
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);      // retrieve() viene de requestHeadersSpec
    }

    // ════════════════════════════════════════════════════════════════════════
    // subirImagen() — éxito
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirImagen() retorna la URL cuando ImgBB responde correctamente")
    void subirImagen_respuestaValida_retornaUrl() throws Exception {
        Map<String, Object> data     = Map.of("url", "https://i.ibb.co/xxxxx/imagen.jpg");
        Map<String, Object> response = Map.of("success", true, "data", data);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "imagen.jpg", "image/jpeg", "bytes-falsos".getBytes()
        );

        String url = service.subirImagen(archivo);

        assertThat(url).isEqualTo("https://i.ibb.co/xxxxx/imagen.jpg");
    }

    // ════════════════════════════════════════════════════════════════════════
    // subirImagen() — ImgBB devuelve success: false
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirImagen() lanza excepción cuando ImgBB devuelve success: false")
    void subirImagen_respuestaFallida_lanzaExcepcion() {
        Map<String, Object> response = Map.of("success", false);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "imagen.jpg", "image/jpeg", "bytes-falsos".getBytes()
        );

        assertThatThrownBy(() -> service.subirImagen(archivo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ImgBB no devolvió una respuesta válida");
    }

    // ════════════════════════════════════════════════════════════════════════
    // subirImagen() — ImgBB devuelve null
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirImagen() lanza excepción cuando ImgBB devuelve null")
    void subirImagen_respuestaNula_lanzaExcepcion() {
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "imagen.jpg", "image/jpeg", "bytes-falsos".getBytes()
        );

        assertThatThrownBy(() -> service.subirImagen(archivo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ImgBB no devolvió una respuesta válida");
    }

    // ════════════════════════════════════════════════════════════════════════
    // subirImagen() — error de red
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirImagen() propaga excepción cuando WebClient falla por red")
    void subirImagen_errorDeRed_propagaExcepcion() {
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("Connection refused")));

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "imagen.jpg", "image/jpeg", "bytes-falsos".getBytes()
        );

        assertThatThrownBy(() -> service.subirImagen(archivo))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Connection refused");
    }

    // ════════════════════════════════════════════════════════════════════════
    // subirImagen() — verifica que se llama a WebClient una sola vez
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirImagen() llama a WebClient exactamente una vez")
    void subirImagen_llamaWebClientUnaVez() throws Exception {
        Map<String, Object> data     = Map.of("url", "https://i.ibb.co/xxxxx/imagen.jpg");
        Map<String, Object> response = Map.of("success", true, "data", data);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "imagen.jpg", "image/jpeg", "bytes-falsos".getBytes()
        );

        service.subirImagen(archivo);

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(anyString());
        verify(requestBodySpec, times(1)).body(any());
        verify(requestHeadersSpec, times(1)).retrieve();  // retrieve() viene de requestHeadersSpec
    }
}