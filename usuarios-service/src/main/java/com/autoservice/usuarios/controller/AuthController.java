package com.autoservice.usuarios.controller;

import com.autoservice.usuarios.dto.ApiResponse;
import com.autoservice.usuarios.dto.CrearUsuarioRequestDTO;
import com.autoservice.usuarios.dto.LoginRequestDTO;
import com.autoservice.usuarios.dto.LoginResponseDTO;
import com.autoservice.usuarios.dto.UsuarioResponseDTO;
import com.autoservice.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Alias de autenticación para el contrato que expone el API Gateway.
 * El CRUD administrativo permanece bajo /api/usuarios.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Autenticación exitosa", usuarioService.login(request))
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> register(
            @Valid @RequestBody CrearUsuarioRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario creado", usuarioService.crear(request)));
    }
}
