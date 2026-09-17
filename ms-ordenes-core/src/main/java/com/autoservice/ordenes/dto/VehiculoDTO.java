package com.autoservice.ordenes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {

    private Long id;
    private Long clienteId;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
}
