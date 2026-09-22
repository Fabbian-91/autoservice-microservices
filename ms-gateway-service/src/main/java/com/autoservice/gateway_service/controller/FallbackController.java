package com.autoservice.gateway_service.controller;

import com.autoservice.gateway_service.common.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*Controlador de fallback para el circuit breaker.
 * Cuando un microservicio falla o el breaker esta abierto, el Gateway
 * redirige la peticion a uno de estos endpoints, que devuelven una
 * respuesta uniforme en lugar de un error generico.
 *
 * Requisito: Manejar casos de servicios caidos.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/usuarios")
    public Mono<ResponseEntity<ApiError>> fallbackUsuarios(ServerWebExchange exchange) {
        return fallback("usuarios", exchange);
    }

    @GetMapping("/clientes")
    public Mono<ResponseEntity<ApiError>> fallbackClientes(ServerWebExchange exchange) {
        return fallback("clientes", exchange);
    }

    @GetMapping("/citas")
    public Mono<ResponseEntity<ApiError>> fallbackCitas(ServerWebExchange exchange) {
        return fallback("citas", exchange);
    }

    @GetMapping("/ordenes")
    public Mono<ResponseEntity<ApiError>> fallbackOrdenes(ServerWebExchange exchange) {
        return fallback("ordenes", exchange);
    }

    @GetMapping("/inventario")
    public Mono<ResponseEntity<ApiError>> fallbackInventario(ServerWebExchange exchange) {
        return fallback("inventario", exchange);
    }

    @GetMapping("/facturacion")
    public Mono<ResponseEntity<ApiError>> fallbackFacturacion(ServerWebExchange exchange) {
        return fallback("facturacion", exchange);
    }

    @GetMapping("/notificaciones")
    public Mono<ResponseEntity<ApiError>> fallbackNotificaciones(ServerWebExchange exchange) {
        return fallback("notificaciones", exchange);
    }

    @GetMapping("/historial")
    public Mono<ResponseEntity<ApiError>> fallbackHistorial(ServerWebExchange exchange) {
        return fallback("historial", exchange);
    }

    //Respuesta uniforme cuando un servicio no esta disponible.
    private Mono<ResponseEntity<ApiError>> fallback(String servicio, ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();

        ApiError error = new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                "El servicio de " + servicio + " no esta disponible en este momento. Intente mas tarde.",
                path
        );

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error));
    }
}
