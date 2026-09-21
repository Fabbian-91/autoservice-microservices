package com.autoservice.gateway_service.common.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

/**
 * Manejo global de errores del Gateway.
 *
 * Captura cualquier excepción que ocurra en el Gateway y devuelve un JSON
 * con el formato ApiError (timestamp, status, error, mensaje, path).
 */
@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();



        // Si la respuesta ya fue iniciada, no podemos escribir mas
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        HttpStatus status;
        String mensaje;

        // Clasificar excepciones segun su tipo
        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());

            // Mensajes personalizados según el código
            if (status == HttpStatus.TOO_MANY_REQUESTS) {
                mensaje = "Demasiadas peticiones. Por favor, intente mas tarde.";
            } else if (status == HttpStatus.NOT_FOUND) {
                mensaje = "La ruta solicitada no existe en el sistema.";
            } else {
                mensaje = rse.getReason() != null ? rse.getReason() : status.getReasonPhrase();
            }

        } else if (ex instanceof TimeoutException) {
            status = HttpStatus.GATEWAY_TIMEOUT;  // 504
            mensaje = "El servicio no respondio a tiempo.";

        } else if (isConnectionError(ex)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;  // 503
            mensaje = "El servicio solicitado no esta disponible en este momento.";

        } else if (ex instanceof java.net.UnknownHostException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;  // 503
            mensaje = "No se pudo contactar al servicio.";

        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;  // 400
            mensaje = ex.getMessage() != null ? ex.getMessage() : "Solicitud invalida.";

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;  // 500
            mensaje = "Error inesperado en el Gateway.";
        }

        // Construir respuesta uniforme

        String path = exchange.getRequest().getURI().getPath();

        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                path
        );

        log.error("✗ [{}] {} → {}: {}",
                exchange.getRequest().getMethod(),
                path,
                status.value(),
                mensaje);

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(apiError);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            String fallback = String.format(
                    "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"mensaje\":\"%s\",\"path\":\"%s\"}",
                    LocalDateTime.now(),
                    status.value(),
                    status.getReasonPhrase(),
                    mensaje,
                    path
            );
            DataBuffer buffer = response.bufferFactory()
                    .wrap(fallback.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    }

    //Detecta errores de microservicios caidos revisando la cadena de causas.
    private boolean isConnectionError(Throwable ex) {
        Throwable cause = ex;
        while (cause != null) {
            String className = cause.getClass().getName();
            if (className.contains("ConnectException")
                    || className.contains("ConnectionRefused")
                    || className.contains("WebClientRequestException")
                    || className.contains("NettyConnect")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
