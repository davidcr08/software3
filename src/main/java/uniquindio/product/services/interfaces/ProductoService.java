package uniquindio.product.services.interfaces;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;

import uniquindio.product.enums.TipoProducto;

import java.util.List;

public interface ProductoService {

    ProductoDetalleDTO crearProducto(CrearProductoDTO productoDTO);
    ProductoDetalleDTO obtenerProducto(String id);
    List<ItemProductoDTO> obtenerProductosPorTipo(TipoProducto tipo);
    ProductoDetalleDTO actualizarProducto(String id, EditarProductoDTO productoDTO);
    void eliminarProducto(String id);
    List<ItemProductoDTO> listarProductos();
}