package com.autoservice.usuarios.controller;

import com.autoservice.usuarios.dto.*;
import com.autoservice.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Alias de autenticación para el contrato que expone el API Gateway.
 * El CRUD administrativo permanece bajo /api/usuarios.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UsuarioService service;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(ApiResponse.ok("Autenticación exitosa", service.login(request)));
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UsuarioResponse>> register(@Valid @RequestBody CrearUsuarioRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.ok("Usuario creado", service.crear(request)));
  }
}
