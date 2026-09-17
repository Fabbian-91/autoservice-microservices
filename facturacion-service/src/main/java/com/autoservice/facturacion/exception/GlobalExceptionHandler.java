package com.autoservice.facturacion.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FacturaNoEncontradaException.class)
    public ResponseEntity<ApiError> handleNoEncontrada(FacturaNoEncontradaException ex) {
        return construirError(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(OrdenYaFacturadaException.class)
    public ResponseEntity<ApiError> handleOrdenYaFacturada(OrdenYaFacturadaException ex) {
        return construirError(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(OrdenNoFinalizadaException.class)
    public ResponseEntity<ApiError> handleOrdenNoFinalizada(OrdenNoFinalizadaException ex) {
        return construirError(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidacion(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return construirError(HttpStatus.BAD_REQUEST, "Datos invalidos", detalles);
    }

    // devolvemos el mismo status que mando Ordenes, no un 500 generico
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleFeign(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        return construirError(status, "Error al consultar el servicio de ordenes: " + ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenerico(Exception ex) {
        return construirError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    private ResponseEntity<ApiError> construirError(HttpStatus status, String mensaje, List<String> detalles) {
        ApiError error = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, detalles);
        return ResponseEntity.status(status).body(error);
    }
}
