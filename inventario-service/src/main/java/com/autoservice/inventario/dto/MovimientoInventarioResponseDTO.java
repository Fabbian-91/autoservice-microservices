package com.autoservice.inventario.dto;

import com.autoservice.inventario.common.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioResponseDTO {

    private Long id;
    private Long repuestoId;
    private String repuestoNombre;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private Long ordenId;
    private LocalDateTime fecha;

}
