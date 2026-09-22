package com.example.citas_service.dto;

import com.example.citas_service.model.EstadoCita;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CitaResponseDTO {

    private Long id;
    private ClienteDTO cliente;
    private VehiculoDTO vehiculo;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private EstadoCita estado;
    private LocalDateTime fechaCreacion;
}
