package com.autoservice.usuarios.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
public record ActualizarUsuarioRequest(String nombre,@Email String correo,@Size(min=8) String password){}
