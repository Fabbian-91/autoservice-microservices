package com.example.citas_service.controller;

import com.example.citas_service.dto.CitaRequestDTO;
import com.example.citas_service.dto.CitaResponseDTO;
import com.example.citas_service.model.Cita;
import com.example.citas_service.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crear(@Valid @RequestBody CitaRequestDTO request) {
        CitaResponseDTO response = citaService.crearCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Cita>> listar() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<CitaResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.confirmarCita(id));
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<CitaResponseDTO> atender(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.atenderCita(id));
    }

    @PatchMapping("/{id}/no-asistio")
    public ResponseEntity<CitaResponseDTO> marcarNoAsistio(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.marcarNoAsistio(id));
    }
}
