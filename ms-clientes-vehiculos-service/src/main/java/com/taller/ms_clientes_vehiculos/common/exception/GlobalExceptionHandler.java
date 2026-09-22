package com.taller.ms_clientes_vehiculos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;


//para manejo de exceptions en los controllers(globalmente)
@RestControllerAdvice
public class GlobalExceptionHandler {

    //para capturar la excepción MethodArgumentNotValidException de las anotaciones de dto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationsException(MethodArgumentNotValidException ex) {
        //Las excepciones de los dto. se disparan con el @valid del controller
        //que devuelve una lista de errores que definimos aquí
        List<String> listaError =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .toList();

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),//CÓDIGO HTTP
                "Bad Request",//ESTADO HTTP
                "Uno o mas campos no son validos",//MSJ PERSONALIZADO
                listaError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    //para manejar cualquier error no controlado, como el 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleErrorException(Exception ex) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocurrió un error inesperado. Por favor, inténtelo de nuevo más tarde o contacte al administrador del sistema." + ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    //para manejar cédula duplicada al crear o actualizar un cliente
    @ExceptionHandler(CedulaDuplicada.class)
    public ResponseEntity<ApiError> handleCedulaDuplicada(CedulaDuplicada ex) {

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    //para manejar cuando no se encuentra un cliente por id o cédula
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ExceptionHandler(VehiculoNotFoundException.class)
    public ResponseEntity<ApiError> handleVehiculoNotFoundException(VehiculoNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(DuplicatePlacaException.class)
    public ResponseEntity<ApiError> handleDuplicatePlacaException(DuplicatePlacaException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }


}
