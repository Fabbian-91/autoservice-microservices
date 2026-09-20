package com.autoservice.usuarios.common.exceptions;

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

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiError> manejarUsuarioNoEncontrado(
            UsuarioNotFoundException exception,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.NOT_FOUND, exception.getMessage(), null, request);
    }

    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<ApiError> manejarCorreoDuplicado(
            CorreoDuplicadoException exception,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.CONFLICT, exception.getMessage(), null, request);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiError> manejarCredencialesInvalidas(
            CredencialesInvalidasException exception,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.UNAUTHORIZED, exception.getMessage(), null, request);
    }

    @ExceptionHandler(RolInvalidoException.class)
    public ResponseEntity<ApiError> manejarRolInvalido(
            RolInvalidoException exception,
            HttpServletRequest request
    ) {
        return crearRespuesta(HttpStatus.BAD_REQUEST, exception.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> detalles = exception.getBindingResult()
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
    public ResponseEntity<ApiError> manejarValidacionDeParametros(
            HandlerMethodValidationException exception,
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
    public ResponseEntity<ApiError> manejarErrorGeneral(
            Exception exception,
            HttpServletRequest request
    ) {
        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servicio de Usuarios",
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
