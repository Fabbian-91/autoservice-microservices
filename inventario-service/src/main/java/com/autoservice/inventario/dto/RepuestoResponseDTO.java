package com.autoservice.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepuestoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidadDisponible;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaActualizacion;

}
