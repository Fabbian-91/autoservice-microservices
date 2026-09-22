package com.example.citas_service.controller;

import com.example.citas_service.dto.CitaRequestDTO;
import com.example.citas_service.dto.CitaResponseDTO;
import com.example.citas_service.dto.CitaUpdateDTO;
import com.example.citas_service.model.EstadoCita;
import com.example.citas_service.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<CitaResponseDTO>> listar(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) EstadoCita estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        if (clienteId != null) {
            return ResponseEntity.ok(citaService.buscarPorCliente(clienteId));
        }
        if (estado != null) {
            return ResponseEntity.ok(citaService.buscarPorEstado(estado));
        }
        if (fecha != null) {
            return ResponseEntity.ok(citaService.buscarPorFecha(fecha));
        }
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody CitaUpdateDTO request) {
        return ResponseEntity.ok(citaService.editarCita(id, request));
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
