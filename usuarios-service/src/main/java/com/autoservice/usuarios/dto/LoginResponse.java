package com.autoservice.usuarios.dto;
public record LoginResponse(String token,String tipo,long expiraEn,String usuario,String rol){}
