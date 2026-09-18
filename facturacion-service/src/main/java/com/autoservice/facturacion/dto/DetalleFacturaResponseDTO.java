package com.autoservice.facturacion.dto;

import com.autoservice.facturacion.enums.TipoDetalle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFacturaResponseDTO {

    private Long id;
    private TipoDetalle tipo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
