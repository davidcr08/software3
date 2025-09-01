package uniquindio.product.services.implementations;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;
import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoDetalleDTO crearProducto(CrearProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setImagenProducto(productoDTO.imagenProducto());
        producto.setCantidad(productoDTO.cantidad());
        producto.setValor(productoDTO.valor());
        producto.setTipo(productoDTO.tipo());
        producto.setUltimaFechaModificacion(LocalDateTime.now());

        Producto productoGuardado = productoRepository.save(producto);
        return convertirAProductoDetalleDTO(productoGuardado);
    }

    @Override
    public ProductoDetalleDTO obtenerProducto(String id) throws ProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("No existe un producto con el ID: " + id));
        return convertirAProductoDetalleDTO(producto);
    }

    @Override
    public List<ItemProductoDTO> obtenerProductosPorTipo(TipoProducto tipo) {
        return productoRepository.findByTipo(tipo).stream()
                .map(this::convertirAItemProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDetalleDTO actualizarProducto(String id, EditarProductoDTO productoDTO) throws ProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("No existe un producto con el ID: " + id));

        producto.setImagenProducto(productoDTO.imagenProducto());
        producto.setCantidad(productoDTO.cantidad());
        producto.setValor(productoDTO.valor());
        producto.setTipo(productoDTO.tipo());
        producto.setUltimaFechaModificacion(LocalDateTime.now());

        Producto productoActualizado = productoRepository.save(producto);
        return convertirAProductoDetalleDTO(productoActualizado);
    }

    @Override
    public void eliminarProducto(String id) throws ProductoException {
        if (!productoRepository.existsById(id)) {
            throw new ProductoException("No existe un producto con el ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<ItemProductoDTO> listarProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertirAItemProductoDTO)
                .collect(Collectors.toList());
    }

    private ItemProductoDTO convertirAItemProductoDTO(Producto producto) {
        return new ItemProductoDTO(
                producto.getIdProducto(),
                producto.getImagenProducto(),
                producto.getCantidad(),
                producto.getValor(),
                producto.getTipo()
        );
    }

    private ProductoDetalleDTO convertirAProductoDetalleDTO(Producto producto) {
        return new ProductoDetalleDTO(
                producto.getIdProducto(),
                producto.getImagenProducto(),
                producto.getCantidad(),
                producto.getUltimaFechaModificacion(),
                producto.getValor(),
                producto.getTipo()
        );
    }
}