package com.taller.ms_historial_service.service.impl;

import com.taller.ms_historial_service.common.exception.EventoHistorialNotFoundException;
import com.taller.ms_historial_service.dto.EventoHistorialResponseDTO;
import com.taller.ms_historial_service.dto.EventoKafkaDTO;
import com.taller.ms_historial_service.enums.TipoEvento;
import com.taller.ms_historial_service.mapper.EventoHistorialMapper;
import com.taller.ms_historial_service.model.EventoHistorial;
import com.taller.ms_historial_service.repository.EventoHistorialRepository;
import com.taller.ms_historial_service.service.contract.IEventoHistorialService;
import com.taller.ms_historial_service.validation.EventoHistorialValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoHistorialServiceImpl implements IEventoHistorialService {

    private final EventoHistorialRepository eventoHistorialRepository;
    private final EventoHistorialMapper eventoHistorialMapper;
    private final EventoHistorialValidation eventoHistorialValidation;

    @Override
    @Transactional
    public EventoHistorialResponseDTO guardarEvento(
            EventoKafkaDTO eventoKafkaDTO,
            String payloadJson) {

        // Valida el evento antes de guardarlo
        eventoHistorialValidation.validarEvento(eventoKafkaDTO);

        // Convierte el DTO recibido a entidad
        EventoHistorial eventoHistorial =
                eventoHistorialMapper.toEntity(eventoKafkaDTO);

        // Guarda una copia del mensaje original de Kafka
        eventoHistorial.setPayloadJson(payloadJson);

        // Registra cuándo Historial recibió el evento
        eventoHistorial.setFecha(LocalDateTime.now());

        EventoHistorial eventoGuardado =
                eventoHistorialRepository.save(eventoHistorial);

        return eventoHistorialMapper.toResponseDTO(eventoGuardado);
    }

    @Override
    public EventoHistorialResponseDTO buscarPorId(Long id) {

        EventoHistorial eventoHistorial =
                eventoHistorialRepository.findById(id)
                        .orElseThrow(() ->
                                new EventoHistorialNotFoundException(
                                        "No se encontró el evento de historial con id: " + id
                                )
                        );

        return eventoHistorialMapper.toResponseDTO(eventoHistorial);
    }

    @Override
    public List<EventoHistorialResponseDTO> listarTodos() {

        List<EventoHistorial> eventos =
                eventoHistorialRepository.findAllByOrderByFechaDesc();

        return eventoHistorialMapper.toResponseDTOList(eventos);
    }


    @Override
    public List<EventoHistorialResponseDTO> listarPorTipoEvento(
            TipoEvento tipoEvento) {

        List<EventoHistorial> eventos =
                eventoHistorialRepository
                        .findByTipoEventoOrderByFechaDesc(tipoEvento);

        return eventoHistorialMapper.toResponseDTOList(eventos);
    }

    @Override
    public List<EventoHistorialResponseDTO> listarPorEntidadId(
            Long entidadId) {

        List<EventoHistorial> eventos =
                eventoHistorialRepository
                        .findByEntidadIdOrderByFechaDesc(entidadId);

        return eventoHistorialMapper.toResponseDTOList(eventos);
    }
}
