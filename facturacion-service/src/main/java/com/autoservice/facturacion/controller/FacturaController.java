package com.autoservice.facturacion.controller;

import com.autoservice.facturacion.common.response.ApiResponse;
import com.autoservice.facturacion.dto.Factura.FacturaRequestDTO;
import com.autoservice.facturacion.dto.Factura.FacturaResponseDTO;
import com.autoservice.facturacion.service.FacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaResponseDTO>>> listarFacturas() {

        List<FacturaResponseDTO> facturas =
                facturaService.listarFacturas();

        ApiResponse<List<FacturaResponseDTO>> response =
                new ApiResponse<>(
                        "Facturas obtenidas correctamente",
                        "200",
                        facturas
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaResponseDTO>> obtenerPorId(
            @PathVariable Long id
    ) {

        FacturaResponseDTO factura =
                facturaService.obtenerPorId(id);

        ApiResponse<FacturaResponseDTO> response =
                new ApiResponse<>(
                        "Factura obtenida correctamente",
                        "200",
                        factura
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacturaResponseDTO>> crearFactura(
            @Valid @RequestBody FacturaRequestDTO requestDTO
    ) {

        FacturaResponseDTO factura =
                facturaService.crearFactura(requestDTO);

        ApiResponse<FacturaResponseDTO> response =
                new ApiResponse<>(
                        "Factura creada correctamente",
                        "201",
                        factura
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaResponseDTO>> actualizarFactura(
            @PathVariable Long id,
            @Valid @RequestBody FacturaRequestDTO requestDTO
    ) {

        FacturaResponseDTO factura =
                facturaService.actualizarFactura(
                        requestDTO,
                        id
                );

        ApiResponse<FacturaResponseDTO> response =
                new ApiResponse<>(
                        "Factura actualizada correctamente",
                        "200",
                        factura
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<FacturaResponseDTO>> anularFactura(
            @PathVariable Long id
    ) {

        FacturaResponseDTO factura =
                facturaService.anularFactura(id);

        ApiResponse<FacturaResponseDTO> response =
                new ApiResponse<>(
                        "Factura anulada correctamente",
                        "200",
                        factura
                );

        return ResponseEntity.ok(response);
    }
}