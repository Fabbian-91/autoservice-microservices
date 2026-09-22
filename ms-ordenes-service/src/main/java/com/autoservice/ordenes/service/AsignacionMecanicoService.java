package com.autoservice.ordenes.service;

import com.autoservice.ordenes.client.UsuariosClient;
import com.autoservice.ordenes.dto.AsignacionMecanicoResponse;
import com.autoservice.ordenes.dto.UsuarioResponse;
import com.autoservice.ordenes.exception.RecursoNoEncontradoException;
import com.autoservice.ordenes.exception.ReglaNegocioException;
import com.autoservice.ordenes.model.Orden;
import com.autoservice.ordenes.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsignacionMecanicoService {

    private final OrdenRepository ordenRepository;
    private final UsuariosClient usuariosClient;

    public AsignacionMecanicoService(
            OrdenRepository ordenRepository,
            UsuariosClient usuariosClient
    ) {
        this.ordenRepository = ordenRepository;
        this.usuariosClient = usuariosClient;
    }

    @Transactional
    public AsignacionMecanicoResponse asignar(
            Long ordenId,
            Long mecanicoId
    ) {

        Orden orden = ordenRepository
                .findById(ordenId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Orden con id " +
                                        ordenId +
                                        " no encontrada"
                        )
                );

        UsuarioResponse usuario =
                usuariosClient.buscarPorId(mecanicoId);

        if (!Boolean.TRUE.equals(usuario.activo())) {

            throw new ReglaNegocioException(
                    "El usuario seleccionado está inactivo"
            );
        }

        if (!"MECANICO".equalsIgnoreCase(usuario.rol())) {

            throw new ReglaNegocioException(
                    "El usuario seleccionado no posee rol MECANICO"
            );
        }

        orden.setMecanicoId(usuario.id());

        ordenRepository.save(orden);

        return new AsignacionMecanicoResponse(
                orden.getId(),
                usuario.id(),
                usuario.nombre()
        );
    }
}