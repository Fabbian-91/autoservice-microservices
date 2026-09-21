package com.autoservice.ordenes.controller;

import com.autoservice.ordenes.dto.ValidacionCierreResponse;
import com.autoservice.ordenes.service.TrabajoOrdenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
public class ValidacionCierreController {

    private final TrabajoOrdenService trabajoService;

    public ValidacionCierreController(
            TrabajoOrdenService trabajoService
    ) {
        this.trabajoService = trabajoService;
    }

    @GetMapping("/{ordenId}/validar-cierre")
    public ValidacionCierreResponse validarCierre(
            @PathVariable Long ordenId
    ) {

        trabajoService.validarCierreOrden(ordenId);

        return new ValidacionCierreResponse(
                ordenId,
                true,
                "La orden puede finalizar porque todos los trabajos están terminados"
        );
    }
}