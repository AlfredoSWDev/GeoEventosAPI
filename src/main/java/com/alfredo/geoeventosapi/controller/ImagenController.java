package com.alfredo.geoeventosapi.controller;

import com.alfredo.geoeventosapi.dto.ImagenResponseDTO;
import com.alfredo.geoeventosapi.service.ImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final ImagenService service;

    public ImagenController(ImagenService service) {
        this.service = service;
    }

    // ── POST /api/imagenes/subir ─────────────────────────────────────────────
    // Recibe multipart/form-data con el campo "archivo"
    // Devuelve la URL pública de ImgBB
    @PostMapping("/subir")
    public ResponseEntity<ImagenResponseDTO> subir(
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            String url = service.subirImagen(archivo);
            return ResponseEntity.ok(
                    new ImagenResponseDTO(true, url, "Imagen subida correctamente")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ImagenResponseDTO(false, null, e.getMessage())
            );
        }
    }
}