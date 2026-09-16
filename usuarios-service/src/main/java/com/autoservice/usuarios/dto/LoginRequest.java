package com.autoservice.usuarios.dto;
import jakarta.validation.constraints.NotBlank;
public record LoginRequest(@NotBlank String usuario,@NotBlank String password){}
