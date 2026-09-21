package com.autoservice.ordenes.kafka;

import java.time.LocalDateTime;

public record OrdenEvento(
        String tipoEvento,
        Long ordenId,
        String descripcion,
        LocalDateTime fecha

) {
}