package uniquindio.product.model.documents;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una bodega física donde se almacenan productos.
 * Usa Lombok para generar getters y setters, y se mapea a la tabla "bodega".
 */
@Getter
@Setter
@Entity
@Table(name = "bodega")
public class Bodega {

    /**
     * Identificador único de la bodega.
     * Se genera automáticamente utilizando UUID y se persiste en la columna "bodega_id".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bodega_id", length = 36)
    private String bodegaId;

    /**
     * Identificador del coordinador responsable de la bodega.
     */
    @Column(name = "coordinador_id")
    private String coordinadorId;

    /**
     * Ciudad donde está ubicada la bodega.
     */
    @Column(name = "ciudad")
    private String ciudad;

    /**
     * Inventario de productos asociados a la bodega.
     * Mapeo unidireccional OneToMany a través de una tabla intermedia "bodega_productos".
     * - joinColumns: referencia a la bodega
     * - inverseJoinColumns: referencia al producto
     */
    @OneToMany
    @JoinTable(
            name = "bodega_productos",
            joinColumns = @JoinColumn(name = "bodega_id", referencedColumnName = "bodega_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id", referencedColumnName = "idProducto")
    )
    private ArrayList<Producto> inventario = new ArrayList<>();
}
