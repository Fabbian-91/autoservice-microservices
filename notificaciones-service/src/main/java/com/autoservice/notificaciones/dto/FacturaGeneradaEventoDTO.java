package com.autoservice.notificaciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Lo que el servicio de Facturacion va a publicar cuando genere una factura.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaGeneradaEventoDTO {

    @NotNull
    private Long clienteId;

    @NotNull
    @Positive
    private BigDecimal total;
}
