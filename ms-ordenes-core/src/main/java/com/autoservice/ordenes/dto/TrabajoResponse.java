package com.autoservice.ordenes.dto;

import com.autoservice.ordenes.model.EstadoTrabajo;

public record TrabajoResponse(
        Long id,
        Long ordenId,
        String descripcion,
        EstadoTrabajo estado

) {
}