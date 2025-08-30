package uniquindio.product.services.interfaces;

import uniquindio.product.dto.pedido.CrearPedidoDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.pedido.PedidoResponseDTO;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido crearPedidoDesdeCarrito(String idCliente, String codigoPasarela) throws CarritoException, ProductoException, PedidoException;
    Pedido crearPedido(CrearPedidoDTO pedidoDTO) throws ProductoException, PedidoException;
    MostrarPedidoDTO mostrarPedido(String idPedido) throws ProductoException, PedidoException;
    Pedido obtenerPedidoPorId(String pedidoId) throws PedidoException;
    List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente) throws ProductoException;
    void eliminarPedido(String id) throws PedidoException;
}