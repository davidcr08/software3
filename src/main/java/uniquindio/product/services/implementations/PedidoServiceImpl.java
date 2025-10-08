package uniquindio.product.services.implementations;

import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import uniquindio.product.dto.pedido.*;
import uniquindio.product.mapper.PedidoMapper;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.EstadoPedido;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Carrito;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetalleCarrito;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.model.vo.Pago;
import uniquindio.product.repositories.CarritoRepository;
import uniquindio.product.repositories.PedidoRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.PasarelaPagoPort;
import uniquindio.product.services.interfaces.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final PasarelaPagoPort pasarelaPagoPort;

    @Override
    public MostrarPedidoDTO crearPedidoDesdeCarrito(String idCliente)
            throws CarritoException, ProductoException {

        Carrito carrito = carritoRepository.findByUsuarioId(idCliente)
                .orElseThrow(() -> new CarritoException("No se encontró el carrito para el usuario con ID: " + idCliente));

        if (carrito.getItems().isEmpty()) {
            throw new CarritoException("El carrito debe tener al menos un producto.");
        }

        // Convertimos los items del carrito en detalle del pedido
        List<DetallePedidoDTO> detallesPedidoDTO = convertirCarritoADetallePedidoDTO(carrito.getItems());

        CrearPedidoDTO pedidoDTO = new CrearPedidoDTO(
                idCliente,
                null, // código pasarela, se llenará al crear la preferencia en MercadoPago
                OffsetDateTime.now(),
                detallesPedidoDTO,
                null // pago inicia en null (aún no procesado por la pasarela)
        );

        MostrarPedidoDTO pedidoCreado = crearPedido(pedidoDTO);

        carrito.vaciar();
        carritoRepository.save(carrito);

        return pedidoCreado;
    }

    private List<DetallePedidoDTO> convertirCarritoADetallePedidoDTO(List<DetalleCarrito> itemsCarrito) {
        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            throw new IllegalArgumentException("La lista de ítems del carrito no puede estar vacía.");
        }

        return itemsCarrito.stream()
                .map(item -> new DetallePedidoDTO(item.getIdProducto(), item.getCantidad()))
                .toList();
    }

    private MostrarPedidoDTO crearPedido(CrearPedidoDTO pedidoDTO) throws ProductoException {
        List<String> idsProductos = pedidoDTO.detallePedido().stream()
                .map(DetallePedidoDTO::idProducto)
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        if (productos.size() != idsProductos.size()) {
            throw new ProductoException("Uno o más productos no existen en la base de datos.");
        }

        Pedido pedido = PedidoMapper.toEntity(pedidoDTO, productos);
        pedido.setEstado(EstadoPedido.PENDIENTE); // Estado inicial
        pedido.setPago(null);          // Pago aún no registrado
        pedido.setCodigoPasarela(null);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return PedidoMapper.toMostrarPedidoDTO(pedidoGuardado, "Cliente Temporal", productos);
    }

    @Override
    public MostrarPedidoDTO mostrarPedido(String idPedido) throws ProductoException, PedidoException {
        Pedido pedido = obtenerPedidoPorId(idPedido);

        // Obtenemos todos los productos referenciados de una sola vez (mejor performance que dentro del for)
        List<String> idsProductos = pedido.getDetalle().stream()
                .map(DetallePedido::getIdProducto)
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        return PedidoMapper.toMostrarPedidoDTO(pedido, productos);
    }


    @Override
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente) throws ProductoException {
        List<Pedido> pedidos = pedidoRepository.findByIdCliente(idCliente);

        List<String> idsProductos = pedidos.stream()
                .flatMap(p -> p.getDetalle().stream().map(DetallePedido::getIdProducto))
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        return pedidos.stream()
                .map(pedido -> PedidoMapper.toPedidoResponseDTO(pedido, productos))
                .toList();
    }

    @Override
    public void eliminarPedido(String id) throws PedidoException {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException(
                        String.format("No se encontró el pedido con ID: %s", id)
                ));

        pedidoRepository.delete(pedido);
    }

    // ==============================
    // Métodos privados auxiliares
    // ==============================

    private Pedido obtenerPedidoPorId(String id) throws PedidoException {
        if (id == null || id.isBlank()) {
            throw new PedidoException("El ID del pedido no puede ser nulo o vacío.");
        }

        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException("No se encontró el pedido con ID: " + id));
    }

    //----------------------PAGO--------------------------------------------------

    @Override
    public Preference realizarPago(String idPedido) throws PedidoException {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado: " + idPedido));

        Preference preference = pasarelaPagoPort.crearPreferencia(pedido);

        pedido.setCodigoPasarela(preference.getId());
        pedidoRepository.save(pedido);

        return preference;
    }

    @Override
    public void recibirNotificacionMercadoPago(NotificacionPagoDTO notificacion) throws PedidoException {
        if (!"payment".equals(notificacion.type())) {
            log.info("Notificación ignorada: {}", notificacion.type());
            return;
        }

        Pago pago = pasarelaPagoPort.obtenerPago(notificacion.data().id());
        String idPedido = pago.getIdPago(); // O extraer del metadata según tu mapper

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado: " + idPedido));

        pedido.setPago(pago);
        pedido.setTotal(pago.getValorTransaccion());
        pedido.setEstado(mapEstadoPedido(pago.getEstado()));

        pedidoRepository.save(pedido);
    }

    private EstadoPedido mapEstadoPedido(EstadoPago estadoPago) {
        return switch (estadoPago) {
            case APROBADO -> EstadoPedido.CONFIRMADO;
            case RECHAZADO -> EstadoPedido.CANCELADO;
            default -> EstadoPedido.PENDIENTE;
        };
    }
}