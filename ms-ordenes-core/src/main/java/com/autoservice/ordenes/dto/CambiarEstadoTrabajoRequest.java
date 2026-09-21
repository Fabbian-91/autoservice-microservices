package com.autoservice.ordenes.dto;

import com.autoservice.ordenes.model.EstadoTrabajo;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoTrabajoRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoTrabajo estado

) {
}