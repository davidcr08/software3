package uniquindio.product.services.interfaces;

import uniquindio.product.enums.TipoProducto;
import uniquindio.product.model.documents.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto crearProducto(Producto producto);
    Optional<Producto> obtenerProducto(String id);
    List<Producto> obtenerProductosPorTipo(TipoProducto tipo);
    List<Producto> obtenerProductosConValorMayorA(Double valorMinimo);
    List<Producto> obtenerProductosConValorMenorA(Double valorMaximo);
    Producto actualizarProducto(Producto producto);
    void eliminarProducto(String id);
    List<Producto> listarProductos();
}