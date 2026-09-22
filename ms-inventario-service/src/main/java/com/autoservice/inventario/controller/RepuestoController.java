package com.autoservice.inventario.controller;

import com.autoservice.inventario.dto.RepuestoRequestDTO;
import com.autoservice.inventario.dto.RepuestoResponseDTO;
import com.autoservice.inventario.service.RepuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
public class RepuestoController {

    private final RepuestoService repuestoService;

    @PostMapping
    public ResponseEntity<RepuestoResponseDTO> crear(
            @Valid @RequestBody RepuestoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repuestoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<RepuestoResponseDTO>> listar() {
        return ResponseEntity.ok(repuestoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(repuestoService.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RepuestoRequestDTO request
    ) {
        return ResponseEntity.ok(repuestoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repuestoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
