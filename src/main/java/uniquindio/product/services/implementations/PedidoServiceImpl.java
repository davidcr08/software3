package uniquindio.product.services.implementations;

import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import uniquindio.product.dto.pedido.*;
import uniquindio.product.exceptions.*;
import uniquindio.product.mapper.PedidoMapper;
import uniquindio.product.model.documents.*;
import uniquindio.product.model.enums.EstadoLote;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.EstadoPedido;
import uniquindio.product.model.vo.DetalleCarrito;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;
import uniquindio.product.repositories.*;
import uniquindio.product.services.interfaces.LoteService;
import uniquindio.product.services.interfaces.PasarelaPagoPort;
import uniquindio.product.services.interfaces.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final PasarelaPagoPort pasarelaPagoPort;
    private final LoteService loteService;
    private final InventarioRepository inventarioRepository;

    private static final String INVENTARIO_ID = "inventario-principal";

    private List<DetallePedidoDTO> convertirCarritoADetallePedidoDTO(List<DetalleCarrito> itemsCarrito) {
        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            throw new IllegalArgumentException("La lista de ítems del carrito no puede estar vacía.");
        }

        return itemsCarrito.stream()
                .map(item -> new DetallePedidoDTO(item.getIdProducto(), item.getCantidad()))
                .toList();
    }

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

    private MostrarPedidoDTO crearPedido(CrearPedidoDTO pedidoDTO)
            throws ProductoException, LoteException {

        List<String> idsProductos = pedidoDTO.detallePedido().stream()
                .map(DetallePedidoDTO::idProducto)
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        if (productos.size() != idsProductos.size()) {
            throw new ProductoException("Uno o más productos no existen en la base de datos.");
        }

        // Seleccionar lotes con FEFO (solo verifica disponibilidad, NO reduce)
        Map<String, Lote> lotesAsignados = new HashMap<>();
        for (DetallePedidoDTO item : pedidoDTO.detallePedido()) {
            // Selecciona el lote más próximo a vencer con stock suficiente
            Lote lote = loteService.seleccionarLoteFEFO(item.idProducto(), item.cantidad());
            lotesAsignados.put(item.idProducto(), lote);
        }

        // Crear pedido con lotes asignados
        Pedido pedido = PedidoMapper.toEntity(pedidoDTO, productos, lotesAsignados);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setPago(null);
        pedido.setCodigoPasarela(null);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        log.info("Pedido creado (stock NO reducido aún): {} - Cliente: {}",
                pedidoGuardado.getId(),
                pedidoDTO.idCliente());

        List<Lote> lotes = new ArrayList<>(lotesAsignados.values());

        return PedidoMapper.toMostrarPedidoDTO(pedidoGuardado, productos, lotes);
    }

    @Override
    public MostrarPedidoDTO mostrarPedido(String idPedido) throws ProductoException, PedidoException {
        Pedido pedido = obtenerPedidoPorId(idPedido);

        List<String> idsProductos = pedido.getDetalle().stream()
                .map(DetallePedido::getIdProducto)
                .distinct()
                .toList();

        List<String> idsLotes = pedido.getDetalle().stream()
                .map(DetallePedido::getIdLote)
                .distinct()
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);
        List<Lote> lotes = loteRepository.findAllById(idsLotes);

        return PedidoMapper.toMostrarPedidoDTO(pedido, productos, lotes);
    }

    @Override
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente) throws ProductoException {
        List<Pedido> pedidos = pedidoRepository.findByIdCliente(idCliente);

        if (pedidos.isEmpty()) {
            return List.of();
        }

        List<String> idsProductos = pedidos.stream()
                .flatMap(p -> p.getDetalle().stream().map(DetallePedido::getIdProducto))
                .distinct()
                .toList();

        List<String> idsLotes = pedidos.stream()
                .flatMap(p -> p.getDetalle().stream().map(DetallePedido::getIdLote))
                .distinct()
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);
        List<Lote> lotes = loteRepository.findAllById(idsLotes);

        return pedidos.stream()
                .map(pedido -> PedidoMapper.toPedidoResponseDTO(pedido, productos, lotes))
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

        // Reducir stock solo si el pago fue aprobado
        if (pago.getEstado() == EstadoPago.APROBADO) {
            try {
                reducirStockDelPedido(pedido);
            } catch (ProductoException | LoteException | InventarioException e) {
                log.error("Error al reducir stock para el pedido {}: {}", idPedido, e.getMessage());
                throw new PedidoException("Error al procesar el inventario del pedido: " + e.getMessage());
            }
        }
        pedidoRepository.save(pedido);
    }

    private void reducirStockDelPedido(Pedido pedido)
            throws ProductoException, LoteException, InventarioException {

        // Obtener inventario
        Inventario inventario = inventarioRepository.findById(INVENTARIO_ID)
                .orElseThrow(() -> new InventarioException("Inventario no inicializado"));

        for (DetallePedido detalle : pedido.getDetalle()) {
            int cantidadRestante = detalle.getCantidad();

            while (cantidadRestante > 0) {
                // Seleccionar lote FEFO
                Lote loteSeleccionado = loteService.seleccionarLoteFEFO(
                        detalle.getIdProducto(),
                        cantidadRestante
                );

                int cantidadLote = loteSeleccionado.getCantidadDisponible();
                int cantidadADescontar = Math.min(cantidadLote, cantidadRestante);

                //Reducir en Inventario (almacén físico)
                inventario.reducirCantidadLote(loteSeleccionado.getId(), cantidadADescontar);

                //Reducir en Lote (registro de producción)
                loteSeleccionado.setCantidadDisponible(
                        loteSeleccionado.getCantidadDisponible() - cantidadADescontar
                );

                if (loteSeleccionado.getCantidadDisponible() == 0) {
                    loteSeleccionado.setEstado(EstadoLote.AGOTADO);
                }

                loteRepository.save(loteSeleccionado);

                cantidadRestante -= cantidadADescontar;

                log.debug("Reducido {} unidades del lote {} (quedan {})",
                        cantidadADescontar,
                        loteSeleccionado.getCodigoLote(),
                        loteSeleccionado.getCantidadDisponible());
            }
        }

        // Guardar inventario actualizado
        inventarioRepository.save(inventario);

        log.info("Stock reducido correctamente (Inventario + Lotes) para el pedido: {}", pedido.getId());
    }

    private EstadoPedido mapEstadoPedido(EstadoPago estadoPago) {
        return switch (estadoPago) {
            case APROBADO -> EstadoPedido.CONFIRMADO;
            case RECHAZADO -> EstadoPedido.CANCELADO;
            default -> EstadoPedido.PENDIENTE;
        };
    }
}