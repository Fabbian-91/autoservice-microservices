package com.example.citas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRegistradaEventoDTO {

    private Long clienteId;
    private LocalDate fecha;
    private LocalTime hora;
}
