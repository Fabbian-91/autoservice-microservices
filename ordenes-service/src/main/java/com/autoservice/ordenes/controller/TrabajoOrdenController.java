package com.autoservice.ordenes.controller;

import com.autoservice.ordenes.dto.ActualizarTrabajoRequest;
import com.autoservice.ordenes.dto.CambiarEstadoTrabajoRequest;
import com.autoservice.ordenes.dto.CrearTrabajoRequest;
import com.autoservice.ordenes.dto.TrabajoResponse;
import com.autoservice.ordenes.service.TrabajoOrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{ordenId}/trabajos")
public class TrabajoOrdenController {

    private final TrabajoOrdenService trabajoService;

    public TrabajoOrdenController(
            TrabajoOrdenService trabajoService
    ) {
        this.trabajoService = trabajoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrabajoResponse agregar(
            @PathVariable Long ordenId,
            @Valid @RequestBody CrearTrabajoRequest request
    ) {

        return trabajoService.agregarTrabajo(
                ordenId,
                request
        );
    }

    @GetMapping
    public List<TrabajoResponse> listar(
            @PathVariable Long ordenId
    ) {

        return trabajoService.listarPorOrden(ordenId);
    }

    @GetMapping("/{trabajoId}")
    public TrabajoResponse buscar(
            @PathVariable Long ordenId,
            @PathVariable Long trabajoId
    ) {

        return trabajoService.buscarPorId(
                ordenId,
                trabajoId
        );
    }

    @PutMapping("/{trabajoId}")
    public TrabajoResponse actualizar(
            @PathVariable Long ordenId,
            @PathVariable Long trabajoId,
            @Valid @RequestBody ActualizarTrabajoRequest request
    ) {

        return trabajoService.actualizar(
                ordenId,
                trabajoId,
                request
        );
    }

    @PatchMapping("/{trabajoId}/estado")
    public TrabajoResponse cambiarEstado(
            @PathVariable Long ordenId,
            @PathVariable Long trabajoId,
            @Valid @RequestBody CambiarEstadoTrabajoRequest request
    ) {

        return trabajoService.cambiarEstado(
                ordenId,
                trabajoId,
                request.estado()
        );
    }

    @DeleteMapping("/{trabajoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long ordenId,
            @PathVariable Long trabajoId
    ) {

        trabajoService.eliminarTrabajo(
                ordenId,
                trabajoId
        );
    }
}