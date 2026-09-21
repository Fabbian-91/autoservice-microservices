package com.autoservice.facturacion.controller;

import com.autoservice.facturacion.dto.Factura.FacturaRequestDTO;
import com.autoservice.facturacion.dto.Factura.FacturaResponseDTO;
import com.autoservice.facturacion.service.FacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarFacturas() {

        return ResponseEntity.ok(
                facturaService.listarFacturas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                facturaService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> crearFactura(
            @Valid @RequestBody FacturaRequestDTO requestDTO
    ) {

        FacturaResponseDTO response =
                facturaService.crearFactura(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}