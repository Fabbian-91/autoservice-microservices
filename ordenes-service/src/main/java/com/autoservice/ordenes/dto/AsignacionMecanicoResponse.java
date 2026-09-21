package com.autoservice.ordenes.dto;

public record AsignacionMecanicoResponse(
        Long ordenId,
        Long mecanicoId,
        String nombreMecanico
) {
}