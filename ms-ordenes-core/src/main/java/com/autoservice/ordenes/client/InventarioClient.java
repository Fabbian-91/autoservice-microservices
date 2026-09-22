package com.autoservice.ordenes.client;

import com.autoservice.ordenes.dto.RepuestoInventarioResponse;
import com.autoservice.ordenes.dto.SalidaInventarioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "inventario-service",
        url = "${services.inventario.url}"
)
public interface InventarioClient {

    @GetMapping("/api/repuestos/{id}")
    RepuestoInventarioResponse obtenerRepuesto(
            @PathVariable("id") Long id
    );

    @PostMapping("/api/inventario/repuestos/{id}/salidas")
    void descontarStock(
            @PathVariable("id") Long id,
            @RequestBody SalidaInventarioRequest request
    );
}