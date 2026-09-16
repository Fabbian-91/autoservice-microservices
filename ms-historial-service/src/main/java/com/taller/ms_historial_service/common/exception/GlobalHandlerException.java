package com.taller.ms_historial_service.common.exception;

import com.taller.ms_historial_service.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {
    // Error al procesar un evento de Kafka
    @ExceptionHandler(EventoKafkaException.class)
    public ResponseEntity<ApiResponse<Object>> handlerKafka(
            EventoKafkaException ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("500", ex.getMessage()));
    }

    // Evento de historial no encontrado
    @ExceptionHandler(EventoHistorialNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlerNotFound(
            EventoHistorialNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("404", ex.getMessage()));
    }

    //Validaciones de Dto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlerValidation(MethodArgumentNotValidException ex) {
        //Captura cada exepción en una lista
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("400", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handlerError(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("500",ex.getMessage()));
    }
}
