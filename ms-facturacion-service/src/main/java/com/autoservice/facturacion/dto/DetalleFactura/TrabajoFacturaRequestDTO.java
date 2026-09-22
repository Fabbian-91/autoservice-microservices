package com.autoservice.facturacion.dto.DetalleFactura;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoFacturaRequestDTO {

    @NotNull(message = "El id del trabajo es obligatorio")
    private Long trabajoId;

    @NotNull(message = "El precio del trabajo es obligatorio")
    @Positive(message = "El precio del trabajo debe ser mayor a cero")
    private BigDecimal precioUnitario;
}