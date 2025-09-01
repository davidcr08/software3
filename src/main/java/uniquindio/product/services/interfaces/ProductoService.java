package uniquindio.product.services.interfaces;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;

import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.exceptions.ProductoException;

import java.util.List;

public interface ProductoService {

    ProductoDetalleDTO crearProducto(CrearProductoDTO productoDTO);
    ProductoDetalleDTO obtenerProducto(String id) throws ProductoException;
    List<ItemProductoDTO> obtenerProductosPorTipo(TipoProducto tipo);
    ProductoDetalleDTO actualizarProducto(String id, EditarProductoDTO productoDTO) throws ProductoException;
    void eliminarProducto(String id) throws ProductoException;
    List<ItemProductoDTO> listarProductos();
}