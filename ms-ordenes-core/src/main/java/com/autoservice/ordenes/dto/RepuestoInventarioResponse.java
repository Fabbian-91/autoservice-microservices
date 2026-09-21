package com.autoservice.ordenes.dto;

import java.math.BigDecimal;

public record RepuestoInventarioResponse(

        Long id,
        String nombre,
        Integer cantidadDisponible,
        BigDecimal precioUnitario

) {
}