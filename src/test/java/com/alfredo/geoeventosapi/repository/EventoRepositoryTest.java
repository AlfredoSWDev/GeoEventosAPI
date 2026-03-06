package com.alfredo.geoeventosapi.repository;

import com.alfredo.geoeventosapi.model.Evento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor; // <- ???
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoRepositoryTest {

    @Mock
    private JdbcTemplate jdbc;

    private EventoRepository repository;

    private Evento eventoEjemplo;

    @BeforeEach
    void setUp() {
        repository = new EventoRepository(jdbc);

        eventoEjemplo = new Evento(
                1,
                "Festival de Verano",
                "USD 15",
                "Plaza Central",
                "Vigente",
                "Concierto al aire libre",
                "https://i.ibb.co/xxxxx/imagen.jpg",
                -33.4489,
                -70.6693
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // findAll()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("findAll() ejecuta SELECT y retorna lista de eventos")
    void findAll_retornaListaDeEventos() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(eventoEjemplo));

        List<Evento> resultado = repository.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreEvento()).isEqualTo("Festival de Verano");
        verify(jdbc, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    @DisplayName("findAll() retorna lista vacía cuando no hay eventos")
    void findAll_retornaListaVacia() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Evento> resultado = repository.findAll();

        assertThat(resultado).isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════
    // search()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("search() ejecuta SELECT con ILIKE y retorna coincidencias")
    void search_retornaEventosCoincidentes() {
        when(jdbc.query(anyString(), any(RowMapper.class), any(), any()))
                .thenReturn(List.of(eventoEjemplo));

        List<Evento> resultado = repository.search("Festival");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreEvento()).isEqualTo("Festival de Verano");
        verify(jdbc).query(anyString(), any(RowMapper.class), eq("%Festival%"), eq("%Festival%"));
    }

    @Test
    @DisplayName("search() retorna lista vacía si no hay coincidencias")
    void search_sinCoincidencias_retornaListaVacia() {
        when(jdbc.query(anyString(), any(RowMapper.class), any(), any()))
                .thenReturn(List.of());

        List<Evento> resultado = repository.search("XYZ");

        assertThat(resultado).isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════
    // findById()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("findById() retorna Optional con evento cuando existe el ID")
    void findById_idExistente_retornaOptional() {
        when(jdbc.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of(eventoEjemplo));

        Optional<Evento> resultado = repository.findById(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEventId()).isEqualTo(1);
        assertThat(resultado.get().getNombreEvento()).isEqualTo("Festival de Verano");
    }

    @Test
    @DisplayName("findById() retorna Optional vacío cuando no existe el ID")
    void findById_idInexistente_retornaOptionalVacio() {
        when(jdbc.query(anyString(), any(RowMapper.class), eq(99)))
                .thenReturn(List.of());

        Optional<Evento> resultado = repository.findById(99);

        assertThat(resultado).isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════
    // save()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("save() ejecuta INSERT y retorna 1 fila afectada")
    void save_insertaEventoYRetorna1() {
        when(jdbc.update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        int filas = repository.save(eventoEjemplo);

        assertThat(filas).isEqualTo(1);
        verify(jdbc, times(1)).update(anyString(),
                eq("Festival de Verano"),
                eq("USD 15"),
                eq("Plaza Central"),
                eq("Vigente"),
                eq("Concierto al aire libre"),
                eq("https://i.ibb.co/xxxxx/imagen.jpg"),
                eq(-33.4489),
                eq(-70.6693)
        );
    }

    @Test
    @DisplayName("save() pasa correctamente campos nulos (fotos y coordenadas)")
    void save_camposNulos_insertaCorrectamente() {
        Evento eventoSinFotos = new Evento(
                null, "Evento Sin Foto", "Gratis", "Parque", "Vigente",
                "Descripcion", null, null, null
        );
        when(jdbc.update(anyString(),
                any(), any(), any(), any(), any(), isNull(), isNull(), isNull()))
                .thenReturn(1);

        int filas = repository.save(eventoSinFotos);

        assertThat(filas).isEqualTo(1);
    }

    // ════════════════════════════════════════════════════════════════════════
    // update()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("update() ejecuta UPDATE y retorna 1 fila afectada")
    void update_actualizaEventoYRetorna1() {
        when(jdbc.update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        int filas = repository.update(1, eventoEjemplo);

        assertThat(filas).isEqualTo(1);
        verify(jdbc, times(1)).update(anyString(),
                eq("Festival de Verano"),
                eq("USD 15"),
                eq("Plaza Central"),
                eq("Vigente"),
                eq("Concierto al aire libre"),
                eq("https://i.ibb.co/xxxxx/imagen.jpg"),
                eq(-33.4489),
                eq(-70.6693),
                eq(1)   // ← el ID va al final en el WHERE
        );
    }

    @Test
    @DisplayName("update() retorna 0 cuando el ID no existe en la BD")
    void update_idInexistente_retorna0() {
        when(jdbc.update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(0);

        int filas = repository.update(99, eventoEjemplo);

        assertThat(filas).isEqualTo(0);
    }

    // ════════════════════════════════════════════════════════════════════════
    // delete()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("delete() ejecuta DELETE y retorna 1 fila afectada")
    void delete_eliminaEventoYRetorna1() {
        when(jdbc.update(anyString(), eq(1))).thenReturn(1);

        int filas = repository.delete(1);

        assertThat(filas).isEqualTo(1);
        verify(jdbc, times(1)).update(anyString(), eq(1));
    }

    @Test
    @DisplayName("delete() retorna 0 cuando el ID no existe en la BD")
    void delete_idInexistente_retorna0() {
        when(jdbc.update(anyString(), eq(99))).thenReturn(0);

        int filas = repository.delete(99);

        assertThat(filas).isEqualTo(0);
    }
}
