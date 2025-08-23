package uniquindio.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.TipoProducto;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idProducto;

    @Column(name = "imagenProducto")
    private String imagenProducto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "ultimaFechaModificacion")
    private LocalDateTime ultimaFechaModificacion;

    @Column(name = "valor")
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoProducto tipo;
}