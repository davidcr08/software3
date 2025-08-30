package uniquindio.product.services.interfaces;

import uniquindio.product.dto.pedido.CrearPedidoDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.pedido.PedidoResponseDTO;
import uniquindio.product.model.documents.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido crearPedidoDesdeCarrito(String idCliente, String codigoPasarela);
    Pedido crearPedido(CrearPedidoDTO pedidoDTO);
    MostrarPedidoDTO mostrarPedido(String idPedido);
    Pedido obtenerPedidoPorId(String pedidoId);
    List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente);
    void eliminarPedido(String id);
}