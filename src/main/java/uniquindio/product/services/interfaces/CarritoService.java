package uniquindio.product.services.interfaces;

import uniquindio.product.dto.carrito.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.ProductoException;

import java.util.List;

public interface CarritoService {
    void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException;
    CarritoDTO agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException;
    CarritoDTO eliminarItemDelCarrito(String idUsuario, String idProducto) throws CarritoException;
    CarritoDTO vaciarCarrito(String idUsuario) throws CarritoException;
    List<InformacionProductoCarritoDTO> listarProductosEnCarrito(String idUsuario) throws CarritoException, ProductoException;
    Double calcularTotalCarrito(String idUsuario) throws CarritoException;
    CarritoResponseDTO obtenerCarritoCompleto(String idUsuario) throws CarritoException, ProductoException;
}