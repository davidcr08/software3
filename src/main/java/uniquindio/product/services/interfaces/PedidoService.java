package uniquindio.product.services.interfaces;

import com.mercadopago.resources.preference.Preference;
import uniquindio.product.dto.pedido.CrearPedidoDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.pedido.PedidoResponseDTO;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Pedido;

import java.util.List;
import java.util.Map;

public interface PedidoService {

    MostrarPedidoDTO crearPedidoDesdeCarrito(String idCliente) throws CarritoException, ProductoException, PedidoException;
    List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente) throws PedidoException;
    void eliminarPedido(String idPedido) throws PedidoException;
    MostrarPedidoDTO mostrarPedido(String idPedido) throws ProductoException, PedidoException;
    Preference realizarPago(String idOrden) throws Exception;
    void recibirNotificacionMercadoPago(Map<String, Object> request);
}
