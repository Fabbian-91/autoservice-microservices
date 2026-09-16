package com.autoservice.usuarios.dto;
import com.autoservice.usuarios.entity.Usuario;
import java.time.Instant;
public record UsuarioResponse(Long id,String nombre,String correo,String rol,boolean activo,Instant fechaCreacion){
  public static UsuarioResponse from(Usuario u){return new UsuarioResponse(u.getId(),u.getNombre(),u.getCorreo(),u.getRol(),u.isActivo(),u.getFechaCreacion());}
}
