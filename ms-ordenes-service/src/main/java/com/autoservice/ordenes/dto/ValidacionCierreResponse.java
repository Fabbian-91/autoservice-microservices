package com.autoservice.ordenes.dto;

public record ValidacionCierreResponse(
        Long ordenId,
        boolean puedeFinalizar,
        String mensaje
) {
}