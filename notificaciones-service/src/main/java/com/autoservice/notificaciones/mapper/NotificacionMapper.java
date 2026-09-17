package com.autoservice.notificaciones.mapper;

import com.autoservice.notificaciones.dto.NotificacionResponseDTO;
import com.autoservice.notificaciones.model.Notificacion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    NotificacionResponseDTO toResponse(Notificacion notificacion);

    List<NotificacionResponseDTO> toResponseList(List<Notificacion> notificaciones);
}
