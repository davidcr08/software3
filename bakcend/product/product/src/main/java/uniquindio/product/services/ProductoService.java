package uniquindio.product.services;

import uniquindio.product.enums.TipoProducto;
import uniquindio.product.model.Producto;

import uniquindio.product.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerProductoPorId(String id) {
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerProductosPorTipo(TipoProducto tipo) {
        return productoRepository.findByTipo(tipo);
    }

    public List<Producto> obtenerProductosConValorMayorA(Double valorMinimo) {
        return productoRepository.findByValorGreaterThan(valorMinimo);
    }

    public List<Producto> obtenerProductosConValorMenorA(Double valorMaximo) {
        return productoRepository.findByValorLessThan(valorMaximo);
    }

    public List<Producto> obtenerProductosConStockMayorA(Integer cantidadMinima) {
        return productoRepository.findByCantidadGreaterThan(cantidadMinima);
    }

    public List<Producto> obtenerProductosConStockMenorA(Integer cantidadMaxima) {
        return productoRepository.findByCantidadLessThan(cantidadMaxima);
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }

    public void eliminarProducto(String id) {
        productoRepository.deleteById(id);
    }

    public boolean existeProducto(String id) {
        return productoRepository.existsById(id);
    }
}