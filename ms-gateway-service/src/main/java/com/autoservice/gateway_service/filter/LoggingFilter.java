package com.autoservice.gateway_service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/*
 * Filtro global que registra un log por cada solicitud que pasa por el Gateway.
 * Requisito: "Registrar un log de cada solicitud que pasa por el Gateway.
 * (ruta, metodo, usuario, tiempo de respuesta)".
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. Guardar el momento de inicio
        exchange.getAttributes().put(START_TIME_ATTR, Instant.now());

        // 2. Extraer info del request
        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String userEmail = request.getHeaders().getFirst("X-User-Email");
        String userRole = request.getHeaders().getFirst("X-User-Role");
        String clientIp = getClientIp(request);

        // 3. Loguear el inicio
        log.info("→ [{}] {} {}{} | user={} | role={} | ip={}",
                method,
                path,
                query != null ? "?" + query : "",
                userEmail != null ? userEmail : "anonimo",
                userRole != null ? userRole : "-",
                clientIp);

        // 4. Ejecutar la cadena y loguear el resultado con el tiempo
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    Instant startTime = exchange.getAttribute(START_TIME_ATTR);
                    if (startTime != null) {
                        long durationMs = Duration.between(startTime, Instant.now()).toMillis();
                        Integer statusCode = exchange.getResponse().getStatusCode() != null
                                ? exchange.getResponse().getStatusCode().value()
                                : 0;

                        log.info("← [{}] {} {} | status={} | {} ms | user={}",
                                method,
                                path,
                                query != null ? "?" + query : "",
                                statusCode,
                                durationMs,
                                userEmail != null ? userEmail : "anonimo");
                    }
                });
    }

    //Extrae la IP real del cliente considerando proxies.
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        return "desconocida";
    }

    @Override
    public int getOrder() {
        // Prioridad mas alta que JwtAuthenticationFilter para loggear todo.
        return -200;
    }
}