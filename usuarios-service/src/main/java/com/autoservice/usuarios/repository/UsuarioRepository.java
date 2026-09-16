package com.autoservice.usuarios.repository;

import com.autoservice.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
  Optional<Usuario> findByCorreoIgnoreCase(String correo);
  boolean existsByCorreoIgnoreCase(String correo);
}
