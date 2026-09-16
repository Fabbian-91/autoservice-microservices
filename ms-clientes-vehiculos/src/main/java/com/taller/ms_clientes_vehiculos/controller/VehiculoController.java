package com.taller.ms_clientes_vehiculos.controller;

import com.taller.ms_clientes_vehiculos.dto.VehiculoRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.VehiculoResponseDTO;
import com.taller.ms_clientes_vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;


    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> getAllVehiculos() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(vehiculoService.getAllVehiculos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> getVehiculoById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(vehiculoService.getVehiculoById(id));
    }


    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> createVehiculo(@Valid @RequestBody VehiculoRequestDTO vehiculoDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculoService.createVehiculo(vehiculoDTO));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> updateVehiculo(@PathVariable Long id, @Valid @RequestBody VehiculoRequestDTO vehiculoDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(vehiculoService.updateVehiculo(id, vehiculoDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> deleteVehiculo(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(vehiculoService.deleteVehiculo(id));
    }


}
