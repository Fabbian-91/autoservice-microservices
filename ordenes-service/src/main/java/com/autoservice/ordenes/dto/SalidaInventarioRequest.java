package com.autoservice.ordenes.dto;

public record SalidaInventarioRequest(
        Integer cantidad,
        Long ordenId
) {
}