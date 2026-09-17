package com.autoservice.ordenes.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(

        LocalDateTime timestamp,
        int status,
        String mensaje,
        List<String> detalles

) {
}