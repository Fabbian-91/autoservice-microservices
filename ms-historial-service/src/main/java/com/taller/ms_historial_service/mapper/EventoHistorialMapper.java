package com.taller.ms_historial_service.mapper;


import com.taller.ms_historial_service.dto.EventoHistorialResponseDTO;
import com.taller.ms_historial_service.dto.EventoKafkaDTO;
import com.taller.ms_historial_service.model.EventoHistorial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoHistorialMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payloadJson", ignore = true)
    EventoHistorial toEntity(EventoKafkaDTO dto);

    EventoHistorialResponseDTO toResponseDTO(EventoHistorial entity);

    List<EventoHistorialResponseDTO> toResponseDTOList(
            List<EventoHistorial> eventos
    );
}