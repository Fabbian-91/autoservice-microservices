package com.autoservice.gateway_service.controller;

import com.autoservice.gateway_service.dto.AggregatedHealthDTO;
import com.autoservice.gateway_service.dto.ServiceHealthDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*Endpoint de salud agregado del Gateway
 *
 * Consulta el /actuator/health de cada uno de los 8 microservicios
 * y devuelve un resumen consolidado
 *
 * Requisito: Exponer un endpoint de salud agregado que
 * resuma el estado de los 8 servicios del Gateway
 */
@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthAggregatorController {

    private final WebClient webClient;

    @Value("${services.usuarios.url:http://localhost:8081}")
    private String usuariosUrl;

    @Value("${services.clientes-vehiculos.url:http://localhost:8082}")
    private String clientesUrl;

    @Value("${services.citas.url:http://localhost:8083}")
    private String citasUrl;

    @Value("${services.ordenes.url:http://localhost:8084}")
    private String ordenesUrl;

    @Value("${services.inventario.url:http://localhost:8085}")
    private String inventarioUrl;

    @Value("${services.facturacion.url:http://localhost:8086}")
    private String facturacionUrl;

    @Value("${services.notificaciones.url:http://localhost:8087}")
    private String notificacionesUrl;

    @Value("${services.historial.url:http://localhost:8088}")
    private String historialUrl;

    public HealthAggregatorController() {
        this.webClient = WebClient.builder()
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(
                        reactor.netty.http.client.HttpClient.create()
                                .responseTimeout(Duration.ofSeconds(3))
                ))
                .build();
    }

    @GetMapping("/services")
    public Mono<AggregatedHealthDTO> healthAgregado() {
        // Lista de servicios a consultar
        Map<String, String> servicios = new LinkedHashMap<>();
        servicios.put("usuarios", usuariosUrl);
        servicios.put("clientes-vehiculos", clientesUrl);
        servicios.put("citas", citasUrl);
        servicios.put("ordenes", ordenesUrl);
        servicios.put("inventario", inventarioUrl);
        servicios.put("facturacion", facturacionUrl);
        servicios.put("notificaciones", notificacionesUrl);
        servicios.put("historial", historialUrl);

        // Consultar todos en paralelo
        List<Mono<ServiceHealthDTO>> checks = servicios.entrySet().stream()
                .map(entry -> checkService(entry.getKey(), entry.getValue()))
                .toList();

        return Flux.merge(checks)
                .collectList()
                .map(this::construirRespuesta);
    }

    //Consulta el /actuator/health de un microservicio
    private Mono<ServiceHealthDTO> checkService(String nombre, String baseUrl) {
        String healthUrl = baseUrl + "/actuator/health";
        Instant inicio = Instant.now();

        return webClient.get()
                .uri(healthUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3))
                .map(body -> {
                    long tiempo = Duration.between(inicio, Instant.now()).toMillis();
                    String estado = body.get("status") != null ? body.get("status").toString() : "UNKNOWN";
                    return new ServiceHealthDTO(nombre, healthUrl, estado, tiempo, "OK");
                })
                .onErrorResume(ex -> {
                    long tiempo = Duration.between(inicio, Instant.now()).toMillis();
                    String detalle = ex.getClass().getSimpleName() + ": " + ex.getMessage();
                    log.warn("Servicio '{}' no disponible: {}", nombre, detalle);
                    return Mono.just(new ServiceHealthDTO(nombre, healthUrl, "DOWN", tiempo, detalle));
                });
    }

    //Construye la respuesta agregada a partir de los estados individuales
    private AggregatedHealthDTO construirRespuesta(List<ServiceHealthDTO> servicios) {
        long arriba = servicios.stream().filter(s -> "UP".equals(s.getEstado())).count();
        long abajo = servicios.size() - arriba;

        String estadoGeneral;
        if (abajo == 0) {
            estadoGeneral = "UP";
        } else if (arriba == 0) {
            estadoGeneral = "DOWN";
        } else {
            estadoGeneral = "DEGRADED";
        }

        Map<String, Integer> resumen = new LinkedHashMap<>();
        resumen.put("total", servicios.size());
        resumen.put("arriba", (int) arriba);
        resumen.put("abajo", (int) abajo);

        return new AggregatedHealthDTO(
                LocalDateTime.now(),
                estadoGeneral,
                servicios,
                resumen
        );
    }
}
