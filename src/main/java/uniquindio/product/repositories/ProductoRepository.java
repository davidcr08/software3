package uniquindio.product.repositories;

import uniquindio.product.enums.TipoProducto;
import uniquindio.product.model.documents.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByTipo(TipoProducto tipo);
    List<Producto> findByValorGreaterThan(Double valorMinimo);
    List<Producto> findByValorLessThan(Double valorMaximo);
    List<Producto> findByCantidadGreaterThan(Integer cantidadMinima);
    List<Producto> findByCantidadLessThan(Integer cantidadMaxima);
}