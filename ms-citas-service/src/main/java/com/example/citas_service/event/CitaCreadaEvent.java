package com.example.citas_service.event;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CitaCreadaEvent {

    private Long citaId;
    private Long clienteId;
    private Long vehiculoId;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDateTime fechaHoraCreacion;

    public CitaCreadaEvent(Long citaId, Long clienteId, Long vehiculoId, LocalDate fecha, LocalTime hora, LocalDateTime fechaHoraCreacion) {
        this.citaId = citaId;
        this.clienteId = clienteId;
        this.vehiculoId = vehiculoId;
        this.fecha = fecha;
        this.hora = hora;
        this.fechaHoraCreacion = fechaHoraCreacion;
    }
}
