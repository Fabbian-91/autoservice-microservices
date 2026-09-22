package com.taller.ms_historial_service.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ApiResponse<T>{
    //Atributos
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    //no se incluyan los atributos cuyo valor sea null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String code,T data) {
        this.code = code;
        this.data = data;
    }
}