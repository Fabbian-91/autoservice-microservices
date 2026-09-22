package com.autoservice.gateway_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Estado de salud individual de un microservicio.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthDTO {

    private String nombre;
    private String url;
    private String estado;          // UP, DOWN, DEGRADED, UNKNOWN
    private long tiempoRespuestaMs;
    private String detalle;
}