package uniquindio.product.services.implementations;

import uniquindio.product.enums.TipoProducto;
import uniquindio.product.model.documents.Producto;

import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> obtenerProducto(String id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No existe un producto con ese ID");
        }
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> obtenerProductosPorTipo(TipoProducto tipo) {
        return productoRepository.findByTipo(tipo);
    }

    @Override
    public List<Producto> obtenerProductosConValorMayorA(Double valorMinimo) {
        return productoRepository.findByValorGreaterThan(valorMinimo);
    }

    @Override
    public List<Producto> obtenerProductosConValorMenorA(Double valorMaximo) {
        return productoRepository.findByValorLessThan(valorMaximo);
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        if (!productoRepository.existsById(producto.getIdProducto())) {
            throw new RuntimeException("No existe un producto con ese ID");
        }
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(String id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No existe un producto con ese ID");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }
}