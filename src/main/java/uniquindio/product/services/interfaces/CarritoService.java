package uniquindio.product.services.interfaces;

import uniquindio.product.dto.carrito.CarritoResponseDTO;
import uniquindio.product.dto.carrito.CrearCarritoDTO;
import uniquindio.product.dto.carrito.DetalleCarritoDTO;
import uniquindio.product.dto.carrito.InformacionProductoCarritoDTO;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Carrito;

import java.util.List;

public interface CarritoService {
    void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException;
    Carrito obtenerCarritoPorUsuario(String idUsuario) throws CarritoException;
    Carrito agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException;
    Carrito eliminarItemDelCarrito(String idUsuario, String idProducto) throws CarritoException;
    Carrito vaciarCarrito(String idUsuario) throws CarritoException;
    List<InformacionProductoCarritoDTO> listarProductosEnCarrito(String idUsuario) throws CarritoException, ProductoException;
    Double calcularTotalCarrito(String idUsuario) throws CarritoException;
    CarritoResponseDTO obtenerCarritoCompleto(String idUsuario) throws CarritoException, ProductoException;
}