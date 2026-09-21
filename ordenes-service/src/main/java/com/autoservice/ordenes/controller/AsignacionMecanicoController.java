package com.autoservice.ordenes.controller;

import com.autoservice.ordenes.dto.AsignacionMecanicoResponse;
import com.autoservice.ordenes.dto.AsignarMecanicoRequest;
import com.autoservice.ordenes.service.AsignacionMecanicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
public class AsignacionMecanicoController {

    private final AsignacionMecanicoService mecanicoService;

    public AsignacionMecanicoController(
            AsignacionMecanicoService mecanicoService
    ) {
        this.mecanicoService = mecanicoService;
    }

    @PutMapping("/{ordenId}/mecanico")
    public AsignacionMecanicoResponse asignar(
            @PathVariable Long ordenId,
            @Valid
            @RequestBody AsignarMecanicoRequest request
    ) {

        return mecanicoService.asignar(
                ordenId,
                request.mecanicoId()
        );
    }
}