package com.autoservice.facturacion.dto;

import com.autoservice.facturacion.enums.EstadoFactura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {

    private Long id;
    private Long ordenId;
    private Long clienteId;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;
    private EstadoFactura estado;
    private List<DetalleFacturaResponseDTO> detalles;
}
