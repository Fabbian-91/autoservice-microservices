package com.autoservice.facturacion.dto.DetalleFactura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// no trae precio, ver mensaje al equipo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoOrdenDTO {
    private Long id;
    private String descripcion;
    private String estado;
}
