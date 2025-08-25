package uniquindio.product.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.TipoProducto;

@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_producto", length = 36)
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