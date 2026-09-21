package com.autoservice.facturacion.kafka;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturaGeneradaEvento {
    private String tipoEvento;

    private Long entidadId;

    private String descripcion;

    private LocalDateTime fecha;
}
