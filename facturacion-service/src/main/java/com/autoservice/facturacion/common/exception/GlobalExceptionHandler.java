package com.autoservice.facturacion.common.exception;

import com.autoservice.facturacion.common.response.ApiError;
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
    public ResponseEntity<ApiError> handleFacturaNoEncontrada(
            FacturaNoEncontradaException ex
    ) {
        return construirError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(OrdenNoEncontradaException.class)
    public ResponseEntity<ApiError> handleOrdenNoEncontrada(
            OrdenNoEncontradaException ex
    ) {
        return construirError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(OrdenYaFacturadaException.class)
    public ResponseEntity<ApiError> handleOrdenYaFacturada(
            OrdenYaFacturadaException ex
    ) {
        return construirError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(OrdenNoFinalizadaException.class)
    public ResponseEntity<ApiError> handleOrdenNoFinalizada(
            OrdenNoFinalizadaException ex
    ) {
        return construirError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(TrabajosPendientesException.class)
    public ResponseEntity<ApiError> handleTrabajosPendientes(
            TrabajosPendientesException ex
    ) {
        return construirError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(PrecioTrabajoException.class)
    public ResponseEntity<ApiError> handlePrecioTrabajo(
            PrecioTrabajoException ex
    ) {
        return construirError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(RepuestosOrdenException.class)
    public ResponseEntity<ApiError> handleRepuestosOrden(
            RepuestosOrdenException ex
    ) {
        return construirError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(FacturaNoCreadaException.class)
    public ResponseEntity<ApiError> handleFacturaNoCreada(
            FacturaNoCreadaException ex
    ) {
        return construirError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(EventoKafkaException.class)
    public ResponseEntity<ApiError> handleEventoKafka(
            EventoKafkaException ex
    ) {
        return construirError(
                HttpStatus.BAD_GATEWAY,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidacion(
            MethodArgumentNotValidException ex
    ) {

        List<String> detalles =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                error.getField()
                                        + ": "
                                        + error.getDefaultMessage()
                        )
                        .toList();

        return construirError(
                HttpStatus.BAD_REQUEST,
                "Datos inválidos",
                detalles
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleFeign(
            FeignException ex
    ) {

        HttpStatus status =
                HttpStatus.resolve(ex.status());

        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }

        return construirError(
                status,
                "Error al consultar el servicio de órdenes: "
                        + ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenerico(
            Exception ex
    ) {

        return construirError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null
        );
    }

    private ResponseEntity<ApiError> construirError(
            HttpStatus status,
            String mensaje,
            List<String> detalles
    ) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                detalles
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(FacturaAnuladaException.class)
    public ResponseEntity<ApiError> handleFacturaAnulada(
            FacturaAnuladaException ex
    ) {
        return construirError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(OrdenNoPerteneceFacturaException.class)
    public ResponseEntity<ApiError> handleOrdenNoPerteneceFactura(
            OrdenNoPerteneceFacturaException ex
    ) {
        return construirError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null
        );
    }
}