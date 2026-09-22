package com.autoservice.facturacion.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaGeneradaEventoDTO {
    private Long clienteId;
    private BigDecimal total;
}
