package com.autoservice.ordenes.service;

import com.autoservice.ordenes.client.InventarioClient;
import com.autoservice.ordenes.dto.*;
import com.autoservice.ordenes.exception.RecursoNoEncontradoException;
import com.autoservice.ordenes.model.Orden;
import com.autoservice.ordenes.model.RepuestoOrden;
import com.autoservice.ordenes.repository.OrdenRepository;
import com.autoservice.ordenes.repository.RepuestoOrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepuestoOrdenService {

    private final RepuestoOrdenRepository repuestoOrdenRepository;
    private final OrdenRepository ordenRepository;
    private final InventarioClient inventarioClient;

    public RepuestoOrdenService(
            RepuestoOrdenRepository repuestoOrdenRepository,
            OrdenRepository ordenRepository,
            InventarioClient inventarioClient
    ) {
        this.repuestoOrdenRepository = repuestoOrdenRepository;
        this.ordenRepository = ordenRepository;
        this.inventarioClient = inventarioClient;
    }

    @Transactional
    public RepuestoOrdenResponse registrar(
            Long ordenId,
            RegistrarRepuestoRequest request
    ) {

        Orden orden = ordenRepository
                .findById(ordenId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Orden con id " + ordenId + " no encontrada"
                        )
                );

        // Inventario valida existencia y stock.
        RepuestoInventarioResponse repuesto =
                inventarioClient.descontarStock(
                        request.repuestoId(),
                        new SalidaInventarioRequest(
                                request.cantidad(),
                                ordenId
                        )
                );

        RepuestoOrden repuestoOrden =
                new RepuestoOrden();

        repuestoOrden.setOrden(orden);
        repuestoOrden.setRepuestoId(request.repuestoId());
        repuestoOrden.setCantidad(request.cantidad());
        repuestoOrden.setPrecioUnitario(
                repuesto.precioUnitario()
        );

        RepuestoOrden guardado =
                repuestoOrdenRepository.save(repuestoOrden);

        return convertirDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<RepuestoOrdenResponse> listar(
            Long ordenId
    ) {

        if (!ordenRepository.existsById(ordenId)) {
            throw new RecursoNoEncontradoException(
                    "Orden con id " + ordenId + " no encontrada"
            );
        }

        return repuestoOrdenRepository
                .findByOrdenId(ordenId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private RepuestoOrdenResponse convertirDTO(
            RepuestoOrden repuesto
    ) {

        return new RepuestoOrdenResponse(
                repuesto.getId(),
                repuesto.getOrden().getId(),
                repuesto.getRepuestoId(),
                repuesto.getCantidad(),
                repuesto.getPrecioUnitario()
        );
    }
}