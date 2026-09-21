package com.taller.ms_historial_service.controller;

import com.taller.ms_historial_service.common.response.ApiResponse;
import com.taller.ms_historial_service.dto.EventoHistorialResponseDTO;
import com.taller.ms_historial_service.enums.TipoEvento;
import com.taller.ms_historial_service.service.contract.IEventoHistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class EventoHistorialController {

    private final IEventoHistorialService eventoHistorialService;


    // Obtiene toda la línea de tiempo
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoHistorialResponseDTO>>> listarTodos() {

        List<EventoHistorialResponseDTO> eventos =
                eventoHistorialService.listarTodos();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "200",
                        "Historial obtenido correctamente",
                        eventos
                )
        );
    }


    // Busca un evento por su id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoHistorialResponseDTO>> buscarPorId(
            @PathVariable Long id) {

        EventoHistorialResponseDTO evento =
                eventoHistorialService.buscarPorId(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "200",
                        "Evento encontrado correctamente",
                        evento
                )
        );
    }


    // Filtra el historial por tipo de evento
    @GetMapping("/tipo/{tipoEvento}")
    public ResponseEntity<ApiResponse<List<EventoHistorialResponseDTO>>> listarPorTipoEvento(
            @PathVariable TipoEvento tipoEvento) {

        List<EventoHistorialResponseDTO> eventos =
                eventoHistorialService.listarPorTipoEvento(tipoEvento);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "200",
                        "Eventos obtenidos correctamente",
                        eventos
                )
        );
    }


    // Busca eventos relacionados con una entidad
    @GetMapping("/entidad/{entidadId}")
    public ResponseEntity<ApiResponse<List<EventoHistorialResponseDTO>>> listarPorEntidadId(
            @PathVariable Long entidadId) {

        List<EventoHistorialResponseDTO> eventos =
                eventoHistorialService.listarPorEntidadId(entidadId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "200",
                        "Eventos de la entidad obtenidos correctamente",
                        eventos
                )
        );
    }
}