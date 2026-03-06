package com.alfredo.geoeventosapi.controller;

import com.alfredo.geoeventosapi.service.ImagenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImagenControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImagenService service;

    @InjectMocks
    private ImagenController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/imagenes/subir — éxito
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /api/imagenes/subir retorna 200 con URL cuando ImgBB responde OK")
    void subir_archivoValido_retorna200ConUrl() throws Exception {
        // Arrange
        String urlEsperada = "https://i.ibb.co/xxxxx/imagen.jpg";
        when(service.subirImagen(any())).thenReturn(urlEsperada);

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo",                     // nombre del campo (@RequestParam)
                "imagen.jpg",                  // nombre del archivo
                MediaType.IMAGE_JPEG_VALUE,    // content-type
                "contenido-falso".getBytes()   // bytes del archivo
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/imagenes/subir").file(archivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.url").value(urlEsperada))
                .andExpect(jsonPath("$.mensaje").value("Imagen subida correctamente"));

        verify(service, times(1)).subirImagen(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/imagenes/subir — error de ImgBB
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /api/imagenes/subir retorna 500 cuando ImgBB falla")
    void subir_servicioLanzaExcepcion_retorna500() throws Exception {
        // Arrange
        when(service.subirImagen(any()))
                .thenThrow(new RuntimeException("ImgBB no devolvió una respuesta válida"));

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo",
                "imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido-falso".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/imagenes/subir").file(archivo))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.url").doesNotExist())
                .andExpect(jsonPath("$.mensaje").value("ImgBB no devolvió una respuesta válida"));

        verify(service, times(1)).subirImagen(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/imagenes/subir — sin archivo
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /api/imagenes/subir sin archivo retorna 400 Bad Request")
    void subir_sinArchivo_retorna400() throws Exception {
        mockMvc.perform(multipart("/api/imagenes/subir"))
                .andExpect(status().isBadRequest());

        verify(service, never()).subirImagen(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/imagenes/subir — archivo vacío
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /api/imagenes/subir con archivo vacío propaga el error como 500")
    void subir_archivoVacio_retorna500() throws Exception {
        when(service.subirImagen(any()))
                .thenThrow(new RuntimeException("El archivo está vacío"));

        MockMultipartFile archivoVacio = new MockMultipartFile(
                "archivo",
                "vacio.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]   // sin bytes
        );

        mockMvc.perform(multipart("/api/imagenes/subir").file(archivoVacio))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.mensaje").value("El archivo está vacío"));
    }
}
