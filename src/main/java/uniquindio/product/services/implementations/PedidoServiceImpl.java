package uniquindio.product.services.implementations;

import uniquindio.product.dto.pedido.*;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.TipoPago;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Carrito;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.enums.Moneda;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Pedido crearPedidoDesdeCarrito(String idCliente, String codigoPasarela) throws CarritoException, ProductoException, PedidoException {
        Carrito carrito = carritoRepository.findByUsuarioId(idCliente)
                .orElseThrow(() -> new CarritoException("No se encontró el carrito para el usuario con ID: " + idCliente));

        if (carrito.getItems().isEmpty()) {
            throw new CarritoException("El carrito debe tener al menos un producto.");
        }

        List<DetallePedidoDTO> detallesPedidoDTO = convertirCarritoADetallePedidoDTO(carrito.getItems());


        PagoDTO pagoDTO = new PagoDTO(
                "PAGO_" + System.currentTimeMillis(),
                Moneda.COP,
                TipoPago.TARJETA_CREDITO,
                "Pendiente de procesar",
                "AUT_" + System.currentTimeMillis(),
                LocalDateTime.now(),
                calcularTotalCarrito(carrito),
                EstadoPago.PENDIENTE,
                "Pasarela de pago" // Metodo de pago
        );

        CrearPedidoDTO pedidoDTO = new CrearPedidoDTO(
                idCliente,
                codigoPasarela,
                LocalDate.now(),
                detallesPedidoDTO,
                pagoDTO // Incluir los datos de pago
        );

        Pedido pedido = crearPedido(pedidoDTO);

        // Vaciar el carrito después de crear el pedido
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return pedido;
    }

    private Double calcularTotalCarrito(Carrito carrito) throws ProductoException {
        Double total = 0.0;
        for (uniquindio.product.model.vo.DetalleCarrito item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ProductoException("Producto no encontrado: " + item.getIdProducto()));
            total += producto.getValor() * item.getCantidad();
        }
        return total;
    }


    @Override
    public Pedido crearPedido(CrearPedidoDTO pedidoDTO) throws ProductoException, PedidoException {
        if (pedidoDTO.detallePedido().isEmpty()) {
            throw new PedidoException("El pedido debe tener al menos un detalle.");
        }

        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoDTO.idCliente());
        pedido.setCodigoPasarela(pedidoDTO.codigoPasarela());
        pedido.setFecha(LocalDate.now());

        List<DetallePedido> detallesPedido = new ArrayList<>();
        Double total = 0.0;

        for (DetallePedidoDTO dto : pedidoDTO.detallePedido()) {
            Producto producto = productoRepository.findById(dto.idProducto())
                    .orElseThrow(() -> new ProductoException("Producto no encontrado: " + dto.idProducto()));

            if (producto.getCantidad() < dto.cantidad()) {
                throw new ProductoException("Stock insuficiente para el producto: " + dto.idProducto());
            }

            // Actualizar stock
            producto.setCantidad(producto.getCantidad() - dto.cantidad());
            productoRepository.save(producto);

            Double subtotal = producto.getValor() * dto.cantidad();
            total += subtotal;

            DetallePedido detalle = new DetallePedido(
                    dto.idProducto(),
                    dto.cantidad(),
                    producto.getValor()
            );
            detallesPedido.add(detalle);
        }

        pedido.setDetalle(detallesPedido);
        pedido.setTotal(total);

        // Configurar los datos de pago
        if (pedidoDTO.pago() != null) {
            Pago pago = new Pago();
            pago.setIdPago(pedidoDTO.pago().idPago());
            pago.setMoneda(pedidoDTO.pago().moneda());
            pago.setTipoPago(pedidoDTO.pago().tipoPago());
            pago.setDetalleEstado(pedidoDTO.pago().detalleEstado());
            pago.setCodigoAutorizacion(pedidoDTO.pago().codigoAutorizacion());
            pago.setFecha(pedidoDTO.pago().fecha());
            pago.setValorTransaccion(pedidoDTO.pago().valorTransaccion());
            pago.setEstado(pedidoDTO.pago().estado());
            pago.setMetodoPago(pedidoDTO.pago().metodoPago());

            pedido.setPago(pago);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public MostrarPedidoDTO mostrarPedido(String idPedido) throws ProductoException, PedidoException {
        Pedido pedido = obtenerPedidoPorId(idPedido);

        List<MostrarDetallePedidoDTO> detalles = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalle()) {
            Producto producto = productoRepository.findById(detalle.getIdProducto())
                    .orElseThrow(() -> new ProductoException("Producto no encontrado: " + detalle.getIdProducto()));

            Double subtotal = detalle.getPrecioUnitario() * detalle.getCantidad();

            detalles.add(new MostrarDetallePedidoDTO(
                    detalle.getIdProducto(),
                    "Producto " + producto.getTipo().toString(),
                    producto.getTipo(),
                    producto.getImagenProducto(),
                    detalle.getPrecioUnitario(),
                    detalle.getCantidad(),
                    subtotal
            ));
        }

        return new MostrarPedidoDTO(
                pedido.getId(),
                "Cliente " + pedido.getIdCliente(),
                pedido.getFecha(),
                pedido.getTotal(),
                detalles
        );
    }

    @Override
    public Pedido obtenerPedidoPorId(String id) throws PedidoException {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException("No se encontró el pedido con ID: " + id));
    }

    @Override
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(String idCliente) throws ProductoException {
        List<Pedido> pedidos = pedidoRepository.findByIdCliente(idCliente);
        List<PedidoResponseDTO> response = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            List<MostrarDetallePedidoDTO> detalles = new ArrayList<>();

            for (DetallePedido detalle : pedido.getDetalle()) {
                Producto producto = productoRepository.findById(detalle.getIdProducto())
                        .orElseThrow(() -> new ProductoException("Producto no encontrado: " + detalle.getIdProducto()));

                Double subtotal = detalle.getPrecioUnitario() * detalle.getCantidad();

                detalles.add(new MostrarDetallePedidoDTO(
                        detalle.getIdProducto(),
                        "Producto " + producto.getTipo().toString(),
                        producto.getTipo(),
                        producto.getImagenProducto(),
                        detalle.getPrecioUnitario(),
                        detalle.getCantidad(),
                        subtotal
                ));
            }

            response.add(new PedidoResponseDTO(
                    pedido.getId(),
                    pedido.getIdCliente(),
                    pedido.getFecha(),
                    pedido.getTotal(),
                    detalles,
                    pedido.getPago()
            ));
        }

        return response;
    }

    @Override
    public void eliminarPedido(String id) throws PedidoException {
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoException("No se encontró el pedido con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private List<DetallePedidoDTO> convertirCarritoADetallePedidoDTO(List<uniquindio.product.model.vo.DetalleCarrito> itemsCarrito) {
        return itemsCarrito.stream()
                .map(item -> new DetallePedidoDTO(
                        item.getIdProducto(),
                        item.getCantidad()
                ))
                .toList();
    }
}