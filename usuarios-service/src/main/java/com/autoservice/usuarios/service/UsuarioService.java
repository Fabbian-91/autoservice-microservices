package com.autoservice.usuarios.service;

import com.autoservice.usuarios.dto.*;
import com.autoservice.usuarios.entity.Usuario;
import com.autoservice.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class UsuarioService {
  private final UsuarioRepository repo; private final PasswordEncoder encoder; private final JwtService jwt;
  public UsuarioResponse crear(CrearUsuarioRequest r){
    if(repo.existsByCorreoIgnoreCase(r.correo())) throw new ResponseStatusException(HttpStatus.CONFLICT,"El correo ya está registrado");
    Usuario u=new Usuario();u.setNombre(r.nombre().trim());u.setCorreo(r.correo().trim().toLowerCase());u.setContrasenaHash(encoder.encode(r.password()));u.setRol(rol(r.rol()));u.setActivo(true);return UsuarioResponse.from(repo.save(u));
  }
  public List<UsuarioResponse> listar(){return repo.findAll().stream().map(UsuarioResponse::from).toList();}
  public UsuarioResponse obtener(Long id){return UsuarioResponse.from(find(id));}
  public UsuarioResponse actualizar(Long id,ActualizarUsuarioRequest r){
    Usuario u=find(id); if(r.nombre()!=null&&!r.nombre().isBlank())u.setNombre(r.nombre().trim());
    if(r.correo()!=null&&!r.correo().equalsIgnoreCase(u.getCorreo())){if(repo.existsByCorreoIgnoreCase(r.correo()))throw new ResponseStatusException(HttpStatus.CONFLICT,"El correo ya está registrado");u.setCorreo(r.correo().trim().toLowerCase());}
    if(r.password()!=null&&!r.password().isBlank())u.setContrasenaHash(encoder.encode(r.password())); return UsuarioResponse.from(repo.save(u));
  }
  public UsuarioResponse rol(Long id,String value){Usuario u=find(id);u.setRol(rol(value));return UsuarioResponse.from(repo.save(u));}
  public UsuarioResponse desactivar(Long id){Usuario u=find(id);u.setActivo(false);return UsuarioResponse.from(repo.save(u));}
  public LoginResponse login(LoginRequest r){Usuario u=repo.findByCorreoIgnoreCase(r.usuario()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario o contraseña incorrectos"));if(!u.isActivo()||!encoder.matches(r.password(),u.getContrasenaHash()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario o contraseña incorrectos");return jwt.issue(u);}
  private Usuario find(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));}
  private String rol(String v){String x=v==null||v.isBlank()?"CLIENTE":v.trim().toUpperCase();if(!List.of("ADMINISTRADOR","RECEPCION","MECANICO","FACTURACION").contains(x))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rol no permitido");return x;}
}
