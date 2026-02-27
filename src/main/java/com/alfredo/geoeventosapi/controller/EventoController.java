package com.alfredo.geoeventosapi.controller;

import com.alfredo.geoeventosapi.dto.EventoDTO;
import com.alfredo.geoeventosapi.model.Evento;
import com.alfredo.geoeventosapi.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    // ── GET /api/eventos ─────────────────────────────────────────────────────
    // Si viene ?q=texto  →  busca
    // Si no viene nada   →  lista todos
    @GetMapping
    public ResponseEntity<List<Evento>> listar(
            @RequestParam(required = false) String q) {

        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(service.buscar(q));
        }
        return ResponseEntity.ok(service.listar());
    }

    // ── GET /api/eventos/{id} ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtener(@PathVariable int id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    // ── POST /api/eventos ────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody EventoDTO dto) {
        service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ── PUT /api/eventos/{id} ────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody EventoDTO dto) {

        service.actualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    // ── DELETE /api/eventos/{id} ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}