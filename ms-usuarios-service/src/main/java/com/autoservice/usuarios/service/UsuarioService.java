package com.autoservice.usuarios.service;

import com.autoservice.usuarios.common.exceptions.CorreoDuplicadoException;
import com.autoservice.usuarios.common.exceptions.CredencialesInvalidasException;
import com.autoservice.usuarios.common.exceptions.RolInvalidoException;
import com.autoservice.usuarios.common.exceptions.UsuarioNotFoundException;
import com.autoservice.usuarios.dto.ActualizarUsuarioRequestDTO;
import com.autoservice.usuarios.dto.CrearUsuarioRequestDTO;
import com.autoservice.usuarios.dto.LoginRequestDTO;
import com.autoservice.usuarios.dto.LoginResponseDTO;
import com.autoservice.usuarios.dto.UsuarioResponseDTO;
import com.autoservice.usuarios.mapper.UsuarioMapper;
import com.autoservice.usuarios.model.Usuario;
import com.autoservice.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponseDTO crear(CrearUsuarioRequestDTO request) {
        if (usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            throw new CorreoDuplicadoException(request.getCorreo());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setCorreo(request.getCorreo().trim().toLowerCase());
        usuario.setContrasenaHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(validarRol(request.getRol()));
        usuario.setActivo(true);

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO obtener(Long id) {
        return usuarioMapper.toResponseDTO(buscarUsuario(id));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, ActualizarUsuarioRequestDTO request) {
        Usuario usuario = buscarUsuario(id);

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            usuario.setNombre(request.getNombre().trim());
        }

        if (request.getCorreo() != null
                && !request.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
            if (usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
                throw new CorreoDuplicadoException(request.getCorreo());
            }
            usuario.setCorreo(request.getCorreo().trim().toLowerCase());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(request.getPassword()));
        }

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO rol(Long id, String rol) {
        Usuario usuario = buscarUsuario(id);
        usuario.setRol(validarRol(rol));
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO desactivar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuario.setActivo(false);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.getUsuario())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!usuario.isActivo()
                || !passwordEncoder.matches(request.getPassword(), usuario.getContrasenaHash())) {
            throw new CredencialesInvalidasException();
        }

        return jwtService.generarToken(usuario);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    private String validarRol(String rol) {
        String rolNormalizado = rol == null || rol.isBlank()
                ? "CLIENTE"
                : rol.trim().toUpperCase();

        if (!List.of("ADMINISTRADOR", "RECEPCION", "MECANICO", "FACTURACION", "CLIENTE")
                .contains(rolNormalizado)) {
            throw new RolInvalidoException(rolNormalizado);
        }

        return rolNormalizado;
    }
}
