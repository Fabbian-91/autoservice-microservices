package com.autoservice.inventario.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RepuestoNotFoundException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(
            RepuestoNotFoundException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler({
            StockInsuficienteException.class,
            RepuestoConMovimientosException.class
    })
    public ResponseEntity<ApiError> manejarConflicto(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.CONFLICT, ex.getMessage(), null, request);
    }

    @ExceptionHandler({
            CantidadFueraDeRangoException.class,
            CantidadInvalidaException.class
    })
    public ResponseEntity<ApiError> manejarCantidadFueraDeRango(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacionCuerpo(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatearError)
                .toList();

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Hay errores en los datos enviados",
                detalles,
                request
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiError> manejarValidacionParametro(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Los parámetros enviados no son válidos",
                null,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(
            Exception ex,
            HttpServletRequest request
    ) {
        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servicio de Inventario",
                null,
                request
        );
    }

    private ResponseEntity<ApiError> crearRespuesta(
            HttpStatus status,
            String mensaje,
            List<String> detalles,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                detalles,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    private String formatearError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

}
