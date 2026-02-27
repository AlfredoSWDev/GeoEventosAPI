package com.alfredo.geoeventosapi.service;

import com.alfredo.geoeventosapi.dto.EventoDTO;
import com.alfredo.geoeventosapi.model.Evento;
import com.alfredo.geoeventosapi.repository.EventoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repo;

    // Spring inyecta el Repository automáticamente
    public EventoService(EventoRepository repo) {
        this.repo = repo;
    }

    // ── Listar todos ────────────────────────────────────────────────────────
    public List<Evento> listar() {
        return repo.findAll();
    }

    // ── Buscar por texto ────────────────────────────────────────────────────
    public List<Evento> buscar(String termino) {
        return repo.search(termino);
    }

    // ── Obtener uno por ID ──────────────────────────────────────────────────
    // Si no existe lanza 404 automáticamente
    public Evento obtener(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Evento no encontrado con id: " + id
                ));
    }

    // ── Crear ───────────────────────────────────────────────────────────────
    public void crear(EventoDTO dto) {
        Evento e = dtoToEvento(dto);
        repo.save(e);
    }

    // ── Actualizar ──────────────────────────────────────────────────────────
    public void actualizar(int id, EventoDTO dto) {
        obtener(id); // valida que existe, si no lanza 404
        Evento e = dtoToEvento(dto);
        repo.update(id, e);
    }

    // ── Eliminar ────────────────────────────────────────────────────────────
    public void eliminar(int id) {
        obtener(id); // valida que existe, si no lanza 404
        repo.delete(id);
    }

    // ── Mapper interno: DTO → Modelo ────────────────────────────────────────
    private Evento dtoToEvento(EventoDTO dto) {
        Evento e = new Evento();
        e.setNombreEvento(dto.getNombreEvento());
        e.setValorEvento(dto.getValorEvento());
        e.setLugarEvento(dto.getLugarEvento());
        e.setVigenciaEvento(dto.getVigenciaEvento());        // antes: setVigencia
        e.setDescripcionEvento(dto.getDescripcionEvento());  // antes: setDescripcion
        e.setFotosEvento(dto.getFotosEvento());              // antes: setFotos
        return e;
    }
}