package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegistrarRepuestoRequest(

        @NotNull(message = "El repuesto es obligatorio")
        Long repuestoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        Integer cantidad

) {
}