package com.autoservice.facturacion.validator;

import com.autoservice.facturacion.dto.OrdenResponseDTO;
import com.autoservice.facturacion.exception.OrdenNoFinalizadaException;
import com.autoservice.facturacion.exception.OrdenYaFacturadaException;
import com.autoservice.facturacion.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacturaValidator {

    private static final String ESTADO_FINALIZADA = "finalizada";

    private final FacturaRepository facturaRepository;

    public void validarPuedeFacturar(OrdenResponseDTO orden) {
        if (!ESTADO_FINALIZADA.equalsIgnoreCase(orden.getEstado())) {
            throw new OrdenNoFinalizadaException(orden.getId());
        }
        if (facturaRepository.existsByOrdenId(orden.getId())) {
            throw new OrdenYaFacturadaException(orden.getId());
        }
    }
}
