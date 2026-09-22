package com.taller.ms_clientes_vehiculos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbCliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'NOMBRE' no puede quedar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe de estar entre los 3 y 100 caracteres.")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El campo 'CEDULA' no puede quedar vacío.")
    @Column(nullable = false, unique = true)
    @Size(min = 8, max = 20, message = "La cedula debe de estar entre los 8 y 20 caracteres.")
    private String cedula;

    @Size(min = 8, max = 20, message = "El teléfono debe de estar entre los 8 y 20 caracteres.")
    private String telefono;

    @Size(min = 3, max = 150, message = "El correo debe de estar entre los 3 y 150 caracteres.")
    private String correo;

    @Size(max = 200, message = "La dirección debe de tener máximo 200 caracteres.")
    private String direccion;

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion;

    //relación, mappedBy referencia al atributo de relación de vehículo
    @OneToMany(mappedBy = "cliente")
    private List<Vehiculo> vehiculos;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDate.now();
    }
}