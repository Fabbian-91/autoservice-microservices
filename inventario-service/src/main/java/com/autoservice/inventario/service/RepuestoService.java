package com.autoservice.inventario.service;

import com.autoservice.inventario.common.exceptions.RepuestoNotFoundException;
import com.autoservice.inventario.dto.RepuestoRequestDTO;
import com.autoservice.inventario.dto.RepuestoResponseDTO;
import com.autoservice.inventario.mapper.RepuestoMapper;
import com.autoservice.inventario.model.Repuesto;
import com.autoservice.inventario.repository.RepuestoRepository;
import com.autoservice.inventario.validation.RepuestoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepuestoService {

    private final RepuestoRepository repuestoRepository;
    private final RepuestoMapper repuestoMapper;
    private final RepuestoValidator repuestoValidator;

    @Transactional
    public RepuestoResponseDTO crear(RepuestoRequestDTO request) {
        Repuesto repuesto = repuestoMapper.toEntity(request);
        repuesto.setNombre(request.getNombre().trim());
        repuesto.setCantidadDisponible(0);

        return repuestoMapper.toResponse(repuestoRepository.save(repuesto));
    }

    public List<RepuestoResponseDTO> listar() {
        return repuestoRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"))
                .stream()
                .map(repuestoMapper::toResponse)
                .toList();
    }

    public RepuestoResponseDTO obtener(Long id) {
        return repuestoMapper.toResponse(buscar(id));
    }

    @Transactional
    public RepuestoResponseDTO actualizar(Long id, RepuestoRequestDTO request) {
        Repuesto repuesto = buscar(id);
        repuestoMapper.actualizarEntidad(request, repuesto);
        repuesto.setNombre(request.getNombre().trim());

        return repuestoMapper.toResponse(repuestoRepository.save(repuesto));
    }

    @Transactional
    public void eliminar(Long id) {
        Repuesto repuesto = buscar(id);

        repuestoValidator.validarQueSePuedeEliminar(id);

        repuestoRepository.delete(repuesto);
    }

    private Repuesto buscar(Long id) {
        return repuestoRepository.findById(id)
                .orElseThrow(() -> new RepuestoNotFoundException(id));
    }

}
