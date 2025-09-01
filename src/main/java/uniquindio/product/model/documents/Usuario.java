package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.model.enums.EstadoCuenta;
import uniquindio.product.model.enums.Rol;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

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

    @Column(name = "rol")
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoCuenta")
    private EstadoCuenta estadoCuenta;
}