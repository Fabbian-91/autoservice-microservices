package com.autoservice.usuarios.controller;

import com.autoservice.usuarios.dto.ActualizarUsuarioRequestDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Autenticación exitosa", usuarioService.login(request))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(
            @Valid @RequestBody CrearUsuarioRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario creado", usuarioService.crear(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok("Usuarios obtenidos", usuarioService.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario obtenido", usuarioService.obtener(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequestDTO request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario actualizado", usuarioService.actualizar(id, request))
        );
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> rol(
            @PathVariable Long id,
            @RequestParam String rol
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Rol actualizado", usuarioService.rol(id, rol))
        );
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario desactivado", usuarioService.desactivar(id))
        );
    }

    @PostMapping("/validar-token")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validar(
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token inválido o ausente"));
        }

        Map<String, Object> datos = Map.of(
                "usuario", jwt.getSubject(),
                "roles", jwt.getClaimAsStringList("roles"),
                "expiraEn", jwt.getExpiresAt()
        );
        return ResponseEntity.ok(ApiResponse.ok("Token válido", datos));
    }
}
