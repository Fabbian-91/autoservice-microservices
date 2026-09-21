package com.autoservice.ordenes.client;

import com.autoservice.ordenes.dto.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "usuarios-service",
        url = "${services.usuarios.url}"
)
public interface UsuariosClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioResponse buscarPorId(
            @PathVariable Long id
    );
}