package uniquindio.product.services.implementations;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import uniquindio.product.dto.pedido.*;
import uniquindio.product.mapper.PagoMapper;
import uniquindio.product.mapper.PedidoMapper;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.TipoPago;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Carrito;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.enums.Moneda;
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

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public MostrarPedidoDTO crearPedidoDesdeCarrito(String idCliente, String codigoPasarela)
            throws CarritoException, ProductoException {

        Carrito carrito = carritoRepository.findByUsuarioId(idCliente)
                .orElseThrow(() -> new CarritoException("No se encontró el carrito para el usuario con ID: " + idCliente));

        if (carrito.getItems().isEmpty()) {
            throw new CarritoException("El carrito debe tener al menos un producto.");
        }

        // Convertimos los items del carrito a detalles DTO
        List<DetallePedidoDTO> detallesPedidoDTO = convertirCarritoADetallePedidoDTO(carrito.getItems());

        // Creamos un pago inicial simulado
        PagoDTO pagoDTO = new PagoDTO(
                "PAGO_" + System.currentTimeMillis(),
                Moneda.COP,
                TipoPago.TARJETA_CREDITO,
                "Pendiente de procesar",
                "AUT_" + System.currentTimeMillis(),
                OffsetDateTime.now(),
                calcularTotalCarrito(carrito),
                EstadoPago.PENDIENTE,
                "Pasarela de pago"
        );

        // Construimos DTO del pedido
        CrearPedidoDTO pedidoDTO = new CrearPedidoDTO(
                idCliente,
                codigoPasarela,
                OffsetDateTime.now(),
                detallesPedidoDTO,
                pagoDTO
        );

        // Creamos el pedido real
        MostrarPedidoDTO pedidoCreado = crearPedido(pedidoDTO);

        // Limpiamos el carrito
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return pedidoCreado;
    }

    /**
     * Convierte los ítems del carrito en una lista de {@link DetallePedidoDTO}.
     *
     * @param itemsCarrito lista de ítems del carrito del usuario.
     * @return lista de detalles del pedido en formato DTO.
     * @throws IllegalArgumentException si la lista de ítems es nula.
     */
    private List<DetallePedidoDTO> convertirCarritoADetallePedidoDTO(List<DetalleCarrito> itemsCarrito) {
        if (itemsCarrito == null) {
            throw new IllegalArgumentException("La lista de ítems del carrito no puede ser nula.");
        }

        return itemsCarrito.stream()
                .map(item -> new DetallePedidoDTO(
                        item.getIdProducto(),
                        item.getCantidad()
                ))
                .toList();
    }
    private MostrarPedidoDTO crearPedido(CrearPedidoDTO pedidoDTO) throws ProductoException {
        // Obtener productos referenciados en el pedido
        List<Producto> productos = productoRepository.findAllById(
                pedidoDTO.detallePedido().stream()
                        .map(DetallePedidoDTO::idProducto)
                        .toList()
        );

        if (productos.size() != pedidoDTO.detallePedido().size()) {
            throw new ProductoException("Uno o más productos no existen en la base de datos.");
        }

        // Convertir DTO → entidad usando el mapper
        Pedido pedido = PedidoMapper.toEntity(pedidoDTO, productos);

        // Guardar pedido en base de datos
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // (Temporal) nombreCliente, luego se integrará con UsuarioService
        String nombreCliente = "Cliente Temporal";

        return PedidoMapper.toMostrarPedidoDTO(pedidoGuardado, nombreCliente, productos);
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

    /*
     * Calcula la cantidad total de productos en una lista de detalles.
     *
     * @param detalles lista de detalles del pedido
     * @return cantidad total de productos

     private int calcularCantidadProductos(List<DetallePedido> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return 0;
        }

        return detalles.stream()
                .filter(detalle -> detalle.getCantidad() != null)
                .mapToInt(DetallePedido::getCantidad)
                .sum();
    }

     * Calcula el valor total del pedido con base en los detalles.
     *
     * @param detalles lista de detalles del pedido
     * @return valor total como BigDecimal

     private BigDecimal valorTotalPedido(List<DetallePedido> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return detalles.stream()
                .filter(detalle -> detalle.getCantidad() != null && detalle.getPrecioUnitario() != null)
                .map(detalle -> {
                    BigDecimal cantidad = BigDecimal.valueOf(detalle.getCantidad());
                    BigDecimal precio = detalle.getPrecioUnitario();
                    return cantidad.multiply(precio);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    **/

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

        // Configurar credenciales (⚠️ esto lo ideal es moverlo a application.properties)
        MercadoPagoConfig.setAccessToken("TOKEN_MERCADO_PAGO");

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
                .notificationUrl("URL_PUBLICA_WEBHOOK")
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

                Pago pago = PagoMapper.toPago(payment);
                pedido.setPago(pago);

                pedido.setTotal(BigDecimal.valueOf(payment.getTransactionAmount().doubleValue()));

                pedidoRepository.save(pedido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}