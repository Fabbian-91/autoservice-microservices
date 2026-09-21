package com.autoservice.ordenes.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError buildApiError(HttpStatus status, String mensaje, HttpServletRequest request) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, null, request.getRequestURI());
    }

    @ExceptionHandler(OrdenNotFoundException.class)
    public ResponseEntity<ApiError> handleOrdenNotFound(OrdenNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(VehiculoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleVehiculoNotFound(VehiculoNoEncontradoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ApiError> handleClienteNotFound(ClienteNoEncontradoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleRecursoNoEncontrado(RecursoNoEncontradoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> handleReglaNegocio(ReglaNegocioException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(OrdenYaEntregadaException.class)
    public ResponseEntity<ApiError> handleOrdenYaEntregada(OrdenYaEntregadaException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildApiError(HttpStatus.CONFLICT, ex.getMessage(), request));
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ApiError> handleTransicionEstadoInvalida(TransicionEstadoInvalidaException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(EstadoOrdenInvalidoException.class)
    public ResponseEntity<ApiError> handleEstadoOrdenInvalido(EstadoOrdenInvalidoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(DiagnosticoRequeridoException.class)
    public ResponseEntity<ApiError> handleDiagnosticoRequerido(DiagnosticoRequeridoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildApiError(HttpStatus.CONFLICT, ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> detalles = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.put(error.getField(), error.getDefaultMessage());
        }
        ApiError apiError = buildApiError(HttpStatus.BAD_REQUEST, "Error de validación de datos", request);
        apiError.setDetalles(String.valueOf(detalles));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(
            org.springframework.web.bind.MissingServletRequestParameterException ex, HttpServletRequest request) {
        String detalle = "Falta el parámetro: " + ex.getParameterName();
        ApiError apiError = buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        apiError.setDetalles(detalle);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> handleRestClientException(RestClientResponseException ex, HttpServletRequest request) {
        String mensaje = "Error al comunicarse con otro microservicio: " + ex.getMessage();
        if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(buildApiError(HttpStatus.NOT_FOUND, mensaje, request));
        }
        log.error("Error RestClient: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildApiError(HttpStatus.SERVICE_UNAVAILABLE, mensaje, request));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiError> handleResourceAccess(ResourceAccessException ex, HttpServletRequest request) {
        String mensaje = "El microservicio externo no está disponible: " + ex.getMessage();
        log.error("Microservicio externo no disponible: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildApiError(HttpStatus.SERVICE_UNAVAILABLE, mensaje, request));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleFeignException(FeignException ex, HttpServletRequest request) {
        String mensaje = "Error al comunicarse con otro microservicio";
        log.error("Error Feign: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(buildApiError(HttpStatus.BAD_GATEWAY, mensaje, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", request));
    }
}