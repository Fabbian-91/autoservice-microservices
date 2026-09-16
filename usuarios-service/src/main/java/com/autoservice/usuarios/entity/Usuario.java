package com.autoservice.usuarios.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity @Table(name="usuario", uniqueConstraints=@UniqueConstraint(name="uk_usuario_correo", columnNames="correo"))
@Getter @Setter @NoArgsConstructor
public class Usuario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=100) private String nombre;
  @Column(nullable=false,length=150) private String correo;
  @Column(name="contrasena_hash",nullable=false,length=255) private String contrasenaHash;
  @Column(nullable=false,length=30) private String rol;
  @Column(nullable=false) private boolean activo=true;
  @Column(name="fecha_creacion",nullable=false) private Instant fechaCreacion;
  @PrePersist void prePersist(){ if(fechaCreacion==null) fechaCreacion=Instant.now(); if(rol==null||rol.isBlank()) rol="CLIENTE"; }
}
