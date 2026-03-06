package com.alfredo.geoeventosapi.service;

import com.alfredo.geoeventosapi.dto.EventoDTO;
import com.alfredo.geoeventosapi.model.Evento;
import com.alfredo.geoeventosapi.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository repo;

    @InjectMocks
    private EventoService service;

    // ── Fixtures reutilizables ───────────────────────────────────────────────

    private Evento eventoEjemplo;
    private EventoDTO dtEjemplo;

    @BeforeEach
    void setUp() {
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

        dtEjemplo = new EventoDTO();
        dtEjemplo.setNombreEvento("Festival de Verano");
        dtEjemplo.setValorEvento("USD 15");
        dtEjemplo.setLugarEvento("Plaza Central");
        dtEjemplo.setVigenciaEvento("Vigente");
        dtEjemplo.setDescripcionEvento("Concierto al aire libre");
        dtEjemplo.setFotosEvento("https://i.ibb.co/xxxxx/imagen.jpg");
        dtEjemplo.setLatitud(-33.4489);
        dtEjemplo.setLongitud(-70.6693);
    }

    // ════════════════════════════════════════════════════════════════════════
    // listar()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("listar() retorna todos los eventos del repositorio")
    void listar_retornaTodosLosEventos() {
        // Arrange
        List<Evento> esperados = List.of(eventoEjemplo);
        when(repo.findAll()).thenReturn(esperados);

        // Act
        List<Evento> resultado = service.listar();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreEvento()).isEqualTo("Festival de Verano");
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("listar() retorna lista vacía cuando no hay eventos")
    void listar_retornaListaVacia() {
        when(repo.findAll()).thenReturn(List.of());

        List<Evento> resultado = service.listar();

        assertThat(resultado).isEmpty();
        verify(repo).findAll();
    }

    // ════════════════════════════════════════════════════════════════════════
    // buscar()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("buscar() delega la búsqueda al repositorio con el término correcto")
    void buscar_delegaAlRepositorio() {
        String termino = "Festival";
        when(repo.search(termino)).thenReturn(List.of(eventoEjemplo));

        List<Evento> resultado = service.buscar(termino);

        assertThat(resultado).hasSize(1);
        verify(repo).search(termino);
    }

    @Test
    @DisplayName("buscar() retorna lista vacía si no hay coincidencias")
    void buscar_sinCoincidencias_retornaListaVacia() {
        when(repo.search("XYZ")).thenReturn(List.of());

        List<Evento> resultado = service.buscar("XYZ");

        assertThat(resultado).isEmpty();
    }

    // ════════════════════════════════════════════════════════════════════════
    // obtener()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("obtener() retorna el evento cuando existe el ID")
    void obtener_idExistente_retornaEvento() {
        when(repo.findById(1)).thenReturn(Optional.of(eventoEjemplo));

        Evento resultado = service.obtener(1);

        assertThat(resultado.getEventId()).isEqualTo(1);
        assertThat(resultado.getNombreEvento()).isEqualTo("Festival de Verano");
    }

    @Test
    @DisplayName("obtener() lanza 404 cuando el ID no existe")
    void obtener_idInexistente_lanza404() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtener(99))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Evento no encontrado con id: 99");
    }

    // ════════════════════════════════════════════════════════════════════════
    // crear()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("crear() mapea el DTO y llama a repo.save() una vez")
    void crear_llamaRepoSaveConDatosCorrectos() {
        service.crear(dtEjemplo);

        // Capturamos el Evento que se le pasó a save()
        verify(repo, times(1)).save(argThat(evento ->
                "Festival de Verano".equals(evento.getNombreEvento()) &&
                        "Plaza Central".equals(evento.getLugarEvento())        &&
                        evento.getLatitud()  != null                           &&
                        evento.getLongitud() != null
        ));
    }

    @Test
    @DisplayName("crear() mapea correctamente los campos de coordenadas")
    void crear_mapeaCoordenadas() {
        service.crear(dtEjemplo);

        verify(repo).save(argThat(evento ->
                Double.compare(evento.getLatitud(),  -33.4489) == 0 &&
                        Double.compare(evento.getLongitud(), -70.6693) == 0
        ));
    }

    // ════════════════════════════════════════════════════════════════════════
    // actualizar()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("actualizar() valida existencia y luego llama a repo.update()")
    void actualizar_idExistente_llamaUpdate() {
        when(repo.findById(1)).thenReturn(Optional.of(eventoEjemplo));

        service.actualizar(1, dtEjemplo);

        verify(repo).findById(1);
        verify(repo, times(1)).update(eq(1), any(Evento.class));
    }

    @Test
    @DisplayName("actualizar() lanza 404 si el evento no existe y NO llama a update()")
    void actualizar_idInexistente_lanza404SinLlamarUpdate() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(99, dtEjemplo))
                .isInstanceOf(ResponseStatusException.class);

        verify(repo, never()).update(anyInt(), any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // eliminar()
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("eliminar() valida existencia y luego llama a repo.delete()")
    void eliminar_idExistente_llamaDelete() {
        when(repo.findById(1)).thenReturn(Optional.of(eventoEjemplo));

        service.eliminar(1);

        verify(repo).findById(1);
        verify(repo, times(1)).delete(1);
    }

    @Test
    @DisplayName("eliminar() lanza 404 si el evento no existe y NO llama a delete()")
    void eliminar_idInexistente_lanza404SinLlamarDelete() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("99");

        verify(repo, never()).delete(anyInt());
    }
}