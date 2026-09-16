package com.autoservice.usuarios.config;

import com.autoservice.usuarios.entity.Usuario;
import com.autoservice.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @RequiredArgsConstructor
public class SeedConfig {
  private final UsuarioRepository repo;private final PasswordEncoder encoder;
  @Bean CommandLineRunner seed(@Value("${app.seed.admin-password}")String admin,@Value("${app.seed.recepcion-password}")String recepcion,@Value("${app.seed.mecanico-password}")String mecanico,@Value("${app.seed.facturacion-password}")String facturacion){return args->{crear("Administradora","admin@autoservice.local",admin,"ADMINISTRADOR");crear("Recepción","recepcion@autoservice.local",recepcion,"RECEPCION");crear("Mecánico","mecanico@autoservice.local",mecanico,"MECANICO");crear("Facturación","facturacion@autoservice.local",facturacion,"FACTURACION");};}
  private void crear(String n,String c,String p,String r){if(repo.existsByCorreoIgnoreCase(c))return;Usuario u=new Usuario();u.setNombre(n);u.setCorreo(c);u.setContrasenaHash(encoder.encode(p));u.setRol(r);u.setActivo(true);repo.save(u);}
}
