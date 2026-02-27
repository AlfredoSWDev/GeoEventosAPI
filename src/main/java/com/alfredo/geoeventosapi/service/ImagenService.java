package com.alfredo.geoeventosapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Service
public class ImagenService {

    // Lee los valores de application.properties
    @Value("${imgbb.api.key}")
    private String apiKey;

    @Value("${imgbb.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    public String subirImagen(MultipartFile archivo) throws Exception {

        // 1. Convertir el archivo a Base64
        String base64 = Base64.getEncoder()
                .encodeToString(archivo.getBytes());

        // 2. Armar el cuerpo del form-data que espera ImgBB
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("key",   apiKey);
        body.add("image", base64);

        // 3. Llamada POST a ImgBB
        Map response = webClient.post()
                .uri(apiUrl)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // bloqueamos porque estamos en contexto Servlet (no reactivo)

        // 4. Extraer la URL de la respuesta JSON
        if (response != null && Boolean.TRUE.equals(response.get("success"))) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            return (String) data.get("url");
        }

        throw new RuntimeException("ImgBB no devolvió una respuesta válida");
    }
}
