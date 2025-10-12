package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.*;
import uniquindio.product.model.vo.DetalleInventario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inventario {


    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idInventario;



    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "detalle_inventario", joinColumns = @JoinColumn(name = "inventario_id"))
    private List<DetalleInventario> detalleInventario = new ArrayList<>();



    public Optional<DetalleInventario> buscarDetallePorProducto(String idProducto) {
        return detalleInventario.stream()
                .filter(detalle -> detalle.getIdProducto().equals(idProducto))
                .findFirst();
    }

    public boolean tieneStockSuficiente(String idProducto, Integer cantidadRequerida) {
        return buscarDetallePorProducto(idProducto)
                .map(detalle -> detalle.getCantidad() >= cantidadRequerida)
                .orElse(false);
    }

    public Integer obtenerStockDisponible(String idProducto) {
        return buscarDetallePorProducto(idProducto)
                .map(DetalleInventario::getCantidad)
                .orElse(0);
    }

    public void actualizarStock(String idProducto, Integer nuevaCantidad) {
        Optional<DetalleInventario> detalleExistente = buscarDetallePorProducto(idProducto);

        if (detalleExistente.isPresent()) {
            DetalleInventario detalle = detalleExistente.get();
            detalle.setCantidad(nuevaCantidad);
        } else {
            DetalleInventario nuevoDetalle = new DetalleInventario(idProducto, nuevaCantidad, LocalDateTime.now());
            detalleInventario.add(nuevoDetalle);
        }

        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void reducirStock(String idProducto, Integer cantidad) {
        Integer stockActual = obtenerStockDisponible(idProducto);
        if (stockActual >= cantidad) {
            actualizarStock(idProducto, stockActual - cantidad);
        } else {
            throw new IllegalStateException("Stock insuficiente para el producto: " + idProducto);
        }
    }


}