package com.autoservice.facturacion.common.exception;

public class OrdenNoPerteneceFacturaException extends RuntimeException {
    public OrdenNoPerteneceFacturaException(
            Long facturaId,
            Long ordenActual,
            Long ordenRecibida
    ) {
        super(
                "La factura con id: %d pertenece a la orden %d y no puede actualizarse con la orden %d"
                        .formatted(facturaId, ordenActual, ordenRecibida)
        );
    }
}
