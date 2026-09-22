package com.example.citas_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaKafkaEvent {

    private String tipoEvento;
    private Long entidadId;
    private String descripcion;
    private LocalDateTime fecha;
}
