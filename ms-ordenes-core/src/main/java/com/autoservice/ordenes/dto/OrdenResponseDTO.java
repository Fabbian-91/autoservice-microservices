package com.autoservice.ordenes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenResponseDTO {

    private Long id;
    private Long citaId;
    private Long clienteId;
    private Long vehiculoId;
    private Long mecanicoId;
    private String motivoIngreso;
    private Integer kilometraje;
    private String diagnostico;
    private String estado;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEntrega;
    private VehiculoDTO vehiculo;
}
