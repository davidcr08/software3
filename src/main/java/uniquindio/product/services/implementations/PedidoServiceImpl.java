package uniquindio.product.services.implementations;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import uniquindio.product.dto.pedido.*;
import uniquindio.product.mapper.PagoMapper;
import uniquindio.product.mapper.PedidoMapper;
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
import uniquindio.product.services.interfaces.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    @Value("${mercadopago.access.token}")
    private String mercadoPagoToken;

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

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

        // 🚨 Aquí ya no generamos pago simulado
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

    /**
     * Calcula el total del carrito sumando el valor de cada producto multiplicado por su cantidad.
     *
     * @param carrito carrito del usuario que contiene los ítems seleccionados.
     * @return total del carrito como BigDecimal.
     * @throws ProductoException si alguno de los productos no existe en la base de datos.
     */
    private BigDecimal calcularTotalCarrito(Carrito carrito) throws ProductoException {
        if (carrito == null || carrito.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return carrito.getItems().stream()
                .map(item -> {
                    Producto producto = productoRepository.findById(item.getIdProducto())
                            .orElseThrow(() -> new ProductoException("Producto no encontrado: " + item.getIdProducto()));

                    // Manejo null-safe del precio (producto.getValor() es Double)
                    Double valorDouble = producto.getValor();
                    BigDecimal precio = valorDouble != null
                            ? BigDecimal.valueOf(valorDouble)
                            : BigDecimal.ZERO;

                    return precio.multiply(BigDecimal.valueOf(item.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //----------------------PAGO--------------------------------------------------

    @Override
    public Preference realizarPago(String idPedido) throws Exception {

        // Obtener el pedido guardado en la base de datos
        Pedido pedidoGuardado = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado: " + idPedido));

        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();

        // Recorrer los detalles del pedido y crear ítems de la pasarela
        for (DetallePedido item : pedidoGuardado.getDetalle()) {

            // Obtener producto asociado al ítem
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ProductoException("Producto no encontrado: " + item.getIdProducto()));

            // Crear ítem de MercadoPago
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(producto.getIdProducto())
                    .title(producto.getNombreProducto())
                    .pictureUrl(producto.getImagenProducto())
                    .categoryId(producto.getTipo().name())
                    .quantity(item.getCantidad())
                    .currencyId("COP")
                    .unitPrice(item.getPrecioUnitario()) // ya es BigDecimal
                    .build();

            itemsPasarela.add(itemRequest);
        }

        // Configurar credenciales
        MercadoPagoConfig.setAccessToken(mercadoPagoToken);

        // Configurar URLs de retorno
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();

        // Construir preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_pedido", pedidoGuardado.getId()))
                .notificationUrl("https://26738d392844.ngrok-free.app")
                .build();

        // Crear preferencia en MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        // Guardar código de pasarela en el pedido
        pedidoGuardado.setCodigoPasarela(preference.getId());
        pedidoRepository.save(pedidoGuardado);

        return preference;
    }

    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {
            Object tipo = request.get("type");

            if ("payment".equals(tipo)) {
                String input = request.get("data").toString();
                String idPago = input.replaceAll("\\D+", "");

                PaymentClient client = new PaymentClient();
                Payment payment = client.get(Long.parseLong(idPago));

                String idPedido = payment.getMetadata().get("id_pedido").toString();

                Pedido pedido = pedidoRepository.findById(idPedido)
                        .orElseThrow(() -> new PedidoException("Pedido no encontrado: " + idPedido));

                // Mapear el pago recibido
                Pago pago = PagoMapper.toPago(payment);
                pedido.setPago(pago);

                // Actualizar total según pasarela
                pedido.setTotal(BigDecimal.valueOf(payment.getTransactionAmount().doubleValue()));

                // Actualizar estado del pedido según estado del pago
                switch (payment.getStatus()) {
                    case "approved" -> pedido.setEstado(EstadoPedido.CONFIRMADO);
                    case "rejected" -> pedido.setEstado(EstadoPedido.CANCELADO);
                    case "in_process" -> pedido.setEstado(EstadoPedido.PENDIENTE);
                    default -> {
                        // Mantener estado actual o loggear
                        System.out.printf("Estado de pago no mapeado: %s%n", payment.getStatus());
                    }
                }

                pedidoRepository.save(pedido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}