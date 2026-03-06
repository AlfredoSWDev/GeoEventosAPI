package com.alfredo.geoeventosapi.controller;

import com.alfredo.geoeventosapi.dto.EventoDTO;
import com.alfredo.geoeventosapi.model.Evento;
import com.alfredo.geoeventosapi.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventoService service;

    @InjectMocks
    private EventoController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Evento eventoEjemplo;
    private EventoDTO dtoEjemplo;

    @BeforeEach
    void setUp() {
        // Sin Spring context — más rápido y sin dependencias externas
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

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

        dtoEjemplo = new EventoDTO();
        dtoEjemplo.setNombreEvento("Festival de Verano");
        dtoEjemplo.setValorEvento("USD 15");
        dtoEjemplo.setLugarEvento("Plaza Central");
        dtoEjemplo.setVigenciaEvento("Vigente");
        dtoEjemplo.setDescripcionEvento("Concierto al aire libre");
        dtoEjemplo.setFotosEvento("https://i.ibb.co/xxxxx/imagen.jpg");
        dtoEjemplo.setLatitud(-33.4489);
        dtoEjemplo.setLongitud(-70.6693);
    }

    // ════════════════════════════════════════════════════════════════════════
    // GET /api/eventos  — listar todos
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /api/eventos retorna 200 y la lista de eventos")
    void listar_retorna200ConLista() throws Exception {
        when(service.listar()).thenReturn(List.of(eventoEjemplo));

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreEvento").value("Festival de Verano"))
                .andExpect(jsonPath("$[0].lugarEvento").value("Plaza Central"));

        verify(service).listar();
    }

    @Test
    @DisplayName("GET /api/eventos retorna 200 con lista vacía cuando no hay eventos")
    void listar_retorna200ListaVacia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ════════════════════════════════════════════════════════════════════════
    // GET /api/eventos?q=texto  — buscar
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /api/eventos?q=Festival retorna 200 y resultados de búsqueda")
    void buscar_conParametroQ_retorna200() throws Exception {
        when(service.buscar("Festival")).thenReturn(List.of(eventoEjemplo));

        mockMvc.perform(get("/api/eventos").param("q", "Festival"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreEvento").value("Festival de Verano"));

        verify(service).buscar("Festival");
        verify(service, never()).listar();
    }

    @Test
    @DisplayName("GET /api/eventos?q=  (espacio en blanco) llama a listar(), no a buscar()")
    void buscar_conQEnBlanco_llamaListar() throws Exception {
        when(service.listar()).thenReturn(List.of(eventoEjemplo));

        mockMvc.perform(get("/api/eventos").param("q", "   "))
                .andExpect(status().isOk());

        verify(service).listar();
        verify(service, never()).buscar(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // GET /api/eventos/{id}
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /api/eventos/1 retorna 200 y el evento correcto")
    void obtener_idExistente_retorna200() throws Exception {
        when(service.obtener(1)).thenReturn(eventoEjemplo);

        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.nombreEvento").value("Festival de Verano"))
                .andExpect(jsonPath("$.latitud").value(-33.4489))
                .andExpect(jsonPath("$.longitud").value(-70.6693));
    }

    @Test
    @DisplayName("GET /api/eventos/99 retorna 404 cuando el evento no existe")
    void obtener_idInexistente_retorna404() throws Exception {
        when(service.obtener(99)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con id: 99")
        );

        mockMvc.perform(get("/api/eventos/99"))
                .andExpect(status().isNotFound());
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/eventos
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /api/eventos retorna 201 Created con body válido")
    void crear_bodyValido_retorna201() throws Exception {
        doNothing().when(service).crear(any(EventoDTO.class));

        mockMvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEjemplo)))
                .andExpect(status().isCreated());

        verify(service, times(1)).crear(any(EventoDTO.class));
    }

    @Test
    @DisplayName("POST /api/eventos con body vacío retorna 400 Bad Request")
    void crear_bodyVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PUT /api/eventos/{id}
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PUT /api/eventos/1 retorna 200 cuando el evento existe")
    void actualizar_idExistente_retorna200() throws Exception {
        doNothing().when(service).actualizar(eq(1), any(EventoDTO.class));

        mockMvc.perform(put("/api/eventos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEjemplo)))
                .andExpect(status().isOk());

        verify(service, times(1)).actualizar(eq(1), any(EventoDTO.class));
    }

    @Test
    @DisplayName("PUT /api/eventos/99 retorna 404 cuando el evento no existe")
    void actualizar_idInexistente_retorna404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con id: 99"))
                .when(service).actualizar(eq(99), any(EventoDTO.class));

        mockMvc.perform(put("/api/eventos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEjemplo)))
                .andExpect(status().isNotFound());
    }

    // ════════════════════════════════════════════════════════════════════════
    // DELETE /api/eventos/{id}
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("DELETE /api/eventos/1 retorna 204 No Content cuando el evento existe")
    void eliminar_idExistente_retorna204() throws Exception {
        doNothing().when(service).eliminar(1);

        mockMvc.perform(delete("/api/eventos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1);
    }

    @Test
    @DisplayName("DELETE /api/eventos/99 retorna 404 cuando el evento no existe")
    void eliminar_idInexistente_retorna404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con id: 99"))
                .when(service).eliminar(99);

        mockMvc.perform(delete("/api/eventos/99"))
                .andExpect(status().isNotFound());
    }
}