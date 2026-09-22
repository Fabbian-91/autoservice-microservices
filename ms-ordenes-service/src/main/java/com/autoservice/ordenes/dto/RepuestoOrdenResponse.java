package com.autoservice.ordenes.dto;

import java.math.BigDecimal;

public record RepuestoOrdenResponse(

        Long id,
        Long ordenId,
        Long repuestoId,
        Integer cantidad,
        BigDecimal precioUnitario

) {
}