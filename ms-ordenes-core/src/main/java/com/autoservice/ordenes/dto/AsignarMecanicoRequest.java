package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.NotNull;

public record AsignarMecanicoRequest(

        @NotNull(message = "El mecánico es obligatorio")
        Long mecanicoId

) {
}