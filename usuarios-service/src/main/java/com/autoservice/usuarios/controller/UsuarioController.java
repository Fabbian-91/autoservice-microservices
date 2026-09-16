package com.autoservice.usuarios.controller;

import com.autoservice.usuarios.dto.*;
import com.autoservice.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/usuarios") @RequiredArgsConstructor
public class UsuarioController {
  private final UsuarioService service;
  @PostMapping("/login") public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest r){return ResponseEntity.ok(ApiResponse.ok("Autenticación exitosa",service.login(r)));}
  @PostMapping public ResponseEntity<ApiResponse<UsuarioResponse>> crear(@Valid @RequestBody CrearUsuarioRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Usuario creado",service.crear(r)));}
  @GetMapping public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listar(){return ResponseEntity.ok(ApiResponse.ok("Usuarios obtenidos",service.listar()));}
  @GetMapping("/{id}") public ResponseEntity<ApiResponse<UsuarioResponse>> obtener(@PathVariable Long id){return ResponseEntity.ok(ApiResponse.ok("Usuario obtenido",service.obtener(id)));}
  @PutMapping("/{id}") public ResponseEntity<ApiResponse<UsuarioResponse>> actualizar(@PathVariable Long id,@Valid @RequestBody ActualizarUsuarioRequest r){return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado",service.actualizar(id,r)));}
  @PatchMapping("/{id}/rol") public ResponseEntity<ApiResponse<UsuarioResponse>> rol(@PathVariable Long id,@RequestParam String rol){return ResponseEntity.ok(ApiResponse.ok("Rol actualizado",service.rol(id,rol)));}
  @PatchMapping("/{id}/desactivar") public ResponseEntity<ApiResponse<UsuarioResponse>> desactivar(@PathVariable Long id){return ResponseEntity.ok(ApiResponse.ok("Usuario desactivado",service.desactivar(id)));}
  @PostMapping("/validar-token") public ResponseEntity<ApiResponse<Map<String,Object>>> validar(@AuthenticationPrincipal Jwt jwt){if(jwt==null)return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Token inválido o ausente"));return ResponseEntity.ok(ApiResponse.ok("Token válido",Map.of("usuario",jwt.getSubject(),"roles",jwt.getClaimAsStringList("roles"),"expiraEn",jwt.getExpiresAt())));}
}
