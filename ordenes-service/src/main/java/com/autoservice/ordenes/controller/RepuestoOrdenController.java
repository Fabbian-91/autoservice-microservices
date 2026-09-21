package com.autoservice.ordenes.controller;

import com.autoservice.ordenes.dto.RegistrarRepuestoRequest;
import com.autoservice.ordenes.dto.RepuestoOrdenResponse;
import com.autoservice.ordenes.service.RepuestoOrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{ordenId}/repuestos")
public class RepuestoOrdenController {

    private final RepuestoOrdenService repuestoService;

    public RepuestoOrdenController(
            RepuestoOrdenService repuestoService
    ) {
        this.repuestoService = repuestoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RepuestoOrdenResponse registrar(
            @PathVariable Long ordenId,
            @Valid
            @RequestBody RegistrarRepuestoRequest request
    ) {

        return repuestoService.registrar(
                ordenId,
                request
        );
    }

    @GetMapping
    public List<RepuestoOrdenResponse> listar(
            @PathVariable Long ordenId
    ) {

        return repuestoService.listar(ordenId);
    }
}