package com.example.citas_service.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaUpdateDTO {

    private LocalDate fecha;

    private LocalTime hora;

    @Size(max = 200, message = "El motivo no puede exceder 200 caracteres")
    private String motivo;
}
