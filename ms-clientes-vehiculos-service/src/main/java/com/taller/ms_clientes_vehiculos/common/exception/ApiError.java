package com.taller.ms_clientes_vehiculos.common.exception;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data//para generar getters y setters automáticamente, esto es útil para inyección de dependencias
public class ApiError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private List<String> detalles;//x sí hay una lista de errores


    //constructor para inicializar
    public ApiError(int status, String error, String message, List<String> detalles) {
        this.timestamp = LocalDateTime.now();//default hora actual, se setea solo cuando se crea el objeto
        this.status = status;
        this.error = error;
        this.message = message;
        this.detalles = detalles;
    }

}
