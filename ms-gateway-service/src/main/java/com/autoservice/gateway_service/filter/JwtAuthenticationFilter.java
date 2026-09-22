package com.autoservice.gateway_service.filter;

import com.autoservice.gateway_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

//Filtro global que intercepta todas las solicitudes que pasan por el Gateway.
//Valida el JWT del header Authorization antes de permitir el paso.
//Cumple con el requisito: "Filtrar autenticación (valida el JWT antes de dejar pasar)"

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;


    //Rutas públicas que NO requieren token JWT.
    //Segun requisitos: solo login/register y health check.
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/actuator",
            "/fallback",
            "api/health"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("→ {} {}", request.getMethod(), path);

        // 1. Rutas publicas: pasar sin validar
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // 2. Extraer header Authorization
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Token no proporcionado o formato invalido", HttpStatus.UNAUTHORIZED);
        }

        // 3. Extraer el token
        String token = authHeader.substring(7);

        // 4. Validar token
        if (!jwtUtil.isTokenValid(token)) {
            return onError(exchange, "Token invalido o expirado", HttpStatus.UNAUTHORIZED);
        }

        // 5. Propagar datos del usuario a los microservicios por los headers
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        Long userId = jwtUtil.extractUserId(token);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Email", username != null ? username : "")
                .header("X-User-Role", role != null ? role : "")
                .header("X-User-Id", userId != null ? userId.toString() : "")
                .build();

        log.debug("Token valido para usuario={} rol={}", username, role);

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    //Devuelve una respuesta uniforme de error con el formato ApiError.
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        //Se crea el JSON manualmente o con ObjectMapper
        String body = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"mensaje\":\"%s\"}",
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );

        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        log.warn("Rechazado {} → {}: {}", exchange.getRequest().getURI().getPath(), status.value(), message);

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        //Esto le da alta prioridad para que se ejecute antes que otros filtros del Gateway
        return -1;
    }
}
