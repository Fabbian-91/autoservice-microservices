package com.autoservice.notificaciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

// Lo que el servicio de Citas va a publicar cuando registre una cita nueva.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRegistradaEventoDTO {

    @NotNull
    private Long clienteId;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;
}
