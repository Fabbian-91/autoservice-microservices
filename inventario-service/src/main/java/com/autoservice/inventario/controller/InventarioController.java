package com.autoservice.inventario.controller;

import com.autoservice.inventario.dto.DisponibilidadResponseDTO;
import com.autoservice.inventario.dto.EntradaInventarioRequestDTO;
import com.autoservice.inventario.dto.MovimientoInventarioResponseDTO;
import com.autoservice.inventario.dto.SalidaInventarioRequestDTO;
import com.autoservice.inventario.service.InventarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping("/repuestos/{repuestoId}/entradas")
    public ResponseEntity<MovimientoInventarioResponseDTO> registrarEntrada(
            @PathVariable Long repuestoId,
            @Valid @RequestBody EntradaInventarioRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventarioService.registrarEntrada(repuestoId, request));
    }

    @PostMapping("/repuestos/{repuestoId}/salidas")
    public ResponseEntity<MovimientoInventarioResponseDTO> registrarSalida(
            @PathVariable Long repuestoId,
            @Valid @RequestBody SalidaInventarioRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventarioService.registrarSalida(repuestoId, request));
    }

    @GetMapping("/repuestos/{repuestoId}/disponibilidad")
    public ResponseEntity<DisponibilidadResponseDTO> consultarDisponibilidad(
            @PathVariable Long repuestoId,
            @RequestParam @Positive(message = "La cantidad debe ser mayor que cero") Integer cantidad
    ) {
        return ResponseEntity.ok(
                inventarioService.consultarDisponibilidad(repuestoId, cantidad)
        );
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(inventarioService.listarMovimientos());
    }

    @GetMapping("/repuestos/{repuestoId}/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponseDTO>> listarPorRepuesto(
            @PathVariable Long repuestoId
    ) {
        return ResponseEntity.ok(inventarioService.listarPorRepuesto(repuestoId));
    }

    @GetMapping("/ordenes/{ordenId}/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponseDTO>> listarPorOrden(
            @PathVariable Long ordenId
    ) {
        return ResponseEntity.ok(inventarioService.listarPorOrden(ordenId));
    }

}
