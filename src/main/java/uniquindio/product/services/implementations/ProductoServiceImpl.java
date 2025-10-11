package uniquindio.product.services.implementations;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;
import uniquindio.product.mapper.ProductoMapper;
import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoDetalleDTO obtenerProductoPorId(String id) throws ProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("El producto con ID " + id + " no existe."));

        return ProductoMapper.toDetalleDTO(producto);
    }

    @Override
    public void crearProducto(CrearProductoDTO productoDTO) throws ProductoException {

        Producto producto = ProductoMapper.toEntity(productoDTO);
        producto.setUltimaFechaModificacion(LocalDateTime.now());

        productoRepository.save(producto);

    }

    @Override
    public List<ItemProductoDTO> obtenerProductosPorTipo(TipoProducto tipo) throws ProductoException {
        List<Producto> productos = productoRepository.findByTipo(tipo);

        if (productos.isEmpty()) {
            throw new ProductoException("No se encontraron productos para el tipo: " + tipo);
        }

        return productos.stream()
                .map(ProductoMapper::toItemDTO)
                .toList();
    }

    @Override
    public void actualizarProducto(String id, EditarProductoDTO productoDTO) throws ProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("El producto con ID " + id + " no existe."));

        // Usamos el mapper para actualizar los campos desde el DTO
        ProductoMapper.updateEntity(producto, productoDTO);

        producto.setUltimaFechaModificacion(LocalDateTime.now());
        productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(String id) throws ProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("El producto con ID " + id + " no existe."));

        productoRepository.delete(producto);
    }

    @Override
    public List<ItemProductoDTO> listarProductos() throws ProductoException {
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            throw new ProductoException("No hay productos registrados.");
        }

        return productos.stream()
                .map(ProductoMapper::toItemDTO)
                .toList();
    }
}