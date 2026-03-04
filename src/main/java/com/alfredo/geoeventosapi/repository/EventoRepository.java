package com.alfredo.geoeventosapi.repository;


import com.alfredo.geoeventosapi.model.Evento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventoRepository {

    private final JdbcTemplate jdbc;

    // Spring inyecta JdbcTemplate automáticamente gracias a @Repository
    public EventoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── RowMapper reutilizable ──────────────────────────────────────────────
    // Convierte cada fila del ResultSet en un objeto Evento
    private final RowMapper<Evento> eventoMapper = (rs, rowNum) -> new Evento(
            rs.getInt("event_id"),
            rs.getString("nombre_evento"),
            rs.getString("valor_evento"),
            rs.getString("lugar_evento"),
            rs.getString("vigencia_evento"),
            rs.getString( "descripcion_evento"),
            rs.getString("fotos_evento"),
            rs.getObject("latitud")  != null ? rs.getDouble("latitud")  : null,
            rs.getObject("longitud") != null ? rs.getDouble("longitud") : null
    );

    // ── SELECT todos ────────────────────────────────────────────────────────
    public List<Evento> findAll() {
        String sql = "SELECT * FROM eventos ORDER BY event_id";
        return jdbc.query(sql, eventoMapper);
    }

    // ── SELECT con búsqueda por nombre o lugar ──────────────────────────────
    public List<Evento> search(String termino) {
        String sql = """
            SELECT * FROM eventos
            WHERE nombre_evento ILIKE ?
               OR lugar_evento  ILIKE ?
            ORDER BY event_id
            """;
        String patron = "%" + termino + "%";
        return jdbc.query(sql, eventoMapper, patron, patron);
    }

    // ── SELECT por ID ───────────────────────────────────────────────────────
    public Optional<Evento> findById(int id) {
        String sql = "SELECT * FROM eventos WHERE event_id = ?";
        List<Evento> result = jdbc.query(sql, eventoMapper, id);
        return result.stream().findFirst();
    }

    // ── INSERT ──
    public int save(Evento e) {
        String sql = """
        INSERT INTO eventos
            (nombre_evento, valor_evento, lugar_evento,
             vigencia_evento, descripcion_evento, fotos_evento, 
             latitud, longitud)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbc.update(sql,
                e.getNombreEvento(),
                e.getValorEvento(),
                e.getLugarEvento(),
                e.getVigenciaEvento(),
                e.getDescripcionEvento(),
                e.getFotosEvento(),
                e.getLatitud(),
                e.getLongitud()
        );
    }


    // ── UPDATE ──
    public int update(int id, Evento e) {
        String sql = """
        UPDATE eventos SET
            nombre_evento      = ?,
            valor_evento       = ?,
            lugar_evento       = ?,
            vigencia_evento    = ?,
            descripcion_evento = ?,
            fotos_evento       = ?,
            latitud            = ?,
            longitud           = ?
        WHERE event_id = ?
        """;
        return jdbc.update(sql,
                e.getNombreEvento(),
                e.getValorEvento(),
                e.getLugarEvento(),
                e.getVigenciaEvento(),
                e.getDescripcionEvento(),
                e.getFotosEvento(),
                e.getLatitud(),
                e.getLongitud(),
                id
        );
    }

    // ── DELETE ──────────────────────────────────────────────────────────────
    public int delete(int id) {
        return jdbc.update("DELETE FROM eventos WHERE event_id = ?", id);
    }
}