package com.taller.ms_clientes_vehiculos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoResponseDTO {

    private Long id;

    private String placa;

    private String marca;

    private String modelo;

    private String color;

    private Integer year;

    private LocalDateTime fechaCreacion;

}
