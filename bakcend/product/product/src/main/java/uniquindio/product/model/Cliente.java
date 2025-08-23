package uniquindio.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.EstadoCuenta;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "cedula", unique = true, nullable = false)
    private String cedula;

    @Column(name = "ciudadDeResidencia")
    private String ciudadDeResidencia;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "correoElectronico", unique = true)
    private String correoElectronico;

    @Column(name = "contrasena")
    private String contrasena;


    @Enumerated(EnumType.STRING)
    @Column(name = "estadoCuenta")
    private EstadoCuenta estadoCuenta;
}