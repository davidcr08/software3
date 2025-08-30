package uniquindio.product.services.interfaces;

import uniquindio.product.dto.carrito.CarritoResponseDTO;
import uniquindio.product.dto.carrito.CrearCarritoDTO;
import uniquindio.product.dto.carrito.DetalleCarritoDTO;
import uniquindio.product.dto.carrito.InformacionProductoCarritoDTO;
import uniquindio.product.model.documents.Carrito;

import java.util.List;

public interface CarritoService {
    void crearCarrito(CrearCarritoDTO carritoDTO);
    Carrito obtenerCarritoPorUsuario(String idUsuario);
    Carrito agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO);
    Carrito eliminarItemDelCarrito(String idUsuario, String idProducto);
    Carrito vaciarCarrito(String idUsuario);
    List<InformacionProductoCarritoDTO> listarProductosEnCarrito(String idUsuario);
    Double calcularTotalCarrito(String idUsuario);
    CarritoResponseDTO obtenerCarritoCompleto(String idUsuario);
}