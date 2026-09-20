package com.autoservice.ordenes.exception;

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

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(
            RecursoNoEncontradoException ex
    ) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                404,
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> manejarReglaNegocio(
            ReglaNegocioException ex
    ) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                400,
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidaciones(
            MethodArgumentNotValidException ex
    ) {

        List<String> detalles =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                error.getField() +
                                        ": " +
                                        error.getDefaultMessage()
                        )
                        .toList();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                400,
                "Datos inválidos",
                detalles
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> manejarFeign(
            FeignException ex
    ) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                502,
                "Error comunicándose con otro microservicio",
                List.of(ex.getMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(error);
    }
}