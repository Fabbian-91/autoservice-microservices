package com.example.citas_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequestDTO {

    @NotNull(message = "El cliente_id es obligatorio")
    private Long clienteId;

    @NotNull(message = "El vehiculo_id es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @Size(max = 200, message = "El motivo no puede exceder 200 caracteres")
    private String motivo;
}
