package com.autoservice.ordenes.controller;

import com.autoservice.ordenes.dto.DiagnosticoDTO;
import com.autoservice.ordenes.dto.OrdenRequestDTO;
import com.autoservice.ordenes.dto.OrdenResponseDTO;
import com.autoservice.ordenes.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.listarPorCliente(clienteId));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenResponseDTO>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(ordenService.listarPorVehiculo(vehiculoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crearOrden(@Valid @RequestBody OrdenRequestDTO dto) {
        OrdenResponseDTO response = ordenService.crearOrden(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/diagnostico")
    public ResponseEntity<OrdenResponseDTO> registrarDiagnostico(@PathVariable Long id,
                                                                 @Valid @RequestBody DiagnosticoDTO dto) {
        return ResponseEntity.ok(ordenService.registrarDiagnostico(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenResponseDTO> cambiarEstado(@PathVariable Long id,
                                                          @RequestParam String estado) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> actualizarOrden(@PathVariable Long id,
                                                            @Valid @RequestBody OrdenRequestDTO dto) {
        return ResponseEntity.ok(ordenService.actualizarOrden(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        ordenService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}