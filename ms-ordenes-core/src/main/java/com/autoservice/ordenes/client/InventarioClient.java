package com.autoservice.ordenes.client;

import com.autoservice.ordenes.dto.RepuestoInventarioResponse;
import com.autoservice.ordenes.dto.SalidaInventarioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "inventario-service",
        url = "${services.inventario.url}"
)
public interface InventarioClient {

    @PostMapping("/api/repuestos/{id}/salida")
    RepuestoInventarioResponse descontarStock(
            @PathVariable Long id,
            @RequestBody SalidaInventarioRequest request
    );
}