package com.autoservice.usuarios.config;

import com.autoservice.usuarios.model.Usuario;
import com.autoservice.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seed(
            @Value("${app.seed.admin-password}") String adminPassword,
            @Value("${app.seed.recepcion-password}") String recepcionPassword,
            @Value("${app.seed.mecanico-password}") String mecanicoPassword,
            @Value("${app.seed.facturacion-password}") String facturacionPassword
    ) {
        return args -> {
            crearUsuario(
                    "Administradora",
                    "admin@autoservice.local",
                    adminPassword,
                    "ADMINISTRADOR"
            );
            crearUsuario(
                    "Recepción",
                    "recepcion@autoservice.local",
                    recepcionPassword,
                    "RECEPCION"
            );
            crearUsuario(
                    "Mecánico",
                    "mecanico@autoservice.local",
                    mecanicoPassword,
                    "MECANICO"
            );
            crearUsuario(
                    "Facturación",
                    "facturacion@autoservice.local",
                    facturacionPassword,
                    "FACTURACION"
            );
        };
    }

    private void crearUsuario(String nombre, String correo, String password, String rol) {
        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
}
