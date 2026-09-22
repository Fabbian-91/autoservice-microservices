package com.autoservice.facturacion.dto.DetalleFactura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepuestoOrdenDTO {
    private Long id;
    private Long repuestoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
