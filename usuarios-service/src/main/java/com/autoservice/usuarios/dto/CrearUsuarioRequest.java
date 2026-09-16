package com.autoservice.usuarios.dto;
import jakarta.validation.constraints.*;
public record CrearUsuarioRequest(@NotBlank String nombre,@NotBlank @Email String correo,@NotBlank @Size(min=8) String password,String rol){}
