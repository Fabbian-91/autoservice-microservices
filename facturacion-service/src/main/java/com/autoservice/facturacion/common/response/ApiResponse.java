package com.autoservice.facturacion.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T> {

    private String message;
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T Data;

    public ApiResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
