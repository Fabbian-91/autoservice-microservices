package com.taller.ms_historial_service.service.contract;

import com.taller.ms_historial_service.dto.EventoHistorialResponseDTO;
import com.taller.ms_historial_service.dto.EventoKafkaDTO;
import com.taller.ms_historial_service.enums.TipoEvento;

import java.util.List;

public interface IEventoHistorialService {
    EventoHistorialResponseDTO guardarEvento(
            EventoKafkaDTO eventoKafkaDTO,
            String payloadJson
    );

    EventoHistorialResponseDTO buscarPorId(Long id);

    List<EventoHistorialResponseDTO> listarTodos();

    List<EventoHistorialResponseDTO> listarPorTipoEvento(
            TipoEvento tipoEvento
    );

    List<EventoHistorialResponseDTO> listarPorEntidadId(
            Long entidadId
    );
}
