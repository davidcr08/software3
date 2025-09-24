package uniquindio.product.mapper;

import uniquindio.product.dto.pedido.*;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class PedidoMapper {

    private PedidoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MostrarPedidoDTO toMostrarPedidoDTO(Pedido pedido, String nombreCliente, List<Producto> productos) {
        Objects.requireNonNull(pedido, "El pedido no puede ser nulo");
        Objects.requireNonNull(productos, "La lista de productos no puede ser nula");

        List<MostrarDetallePedidoDTO> detalles = pedido.getDetalle().stream()
                .map(detalle -> toMostrarDetallePedidoDTO(detalle, productos))
                .toList();

        return new MostrarPedidoDTO(
                pedido.getId(),
                nombreCliente,
                pedido.getFechaCreacion(),
                pedido.getTotal(),
                detalles
        );
    }

    public static MostrarPedidoDTO toMostrarPedidoDTO(Pedido pedido, List<Producto> productos) {
        return toMostrarPedidoDTO(pedido, pedido.getIdCliente(), productos);
    }

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido, List<Producto> productos) {
        Objects.requireNonNull(pedido, "El pedido no puede ser nulo");

        List<MostrarDetallePedidoDTO> detalles = pedido.getDetalle().stream()
                .map(detalle -> toMostrarDetallePedidoDTO(detalle, productos))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getIdCliente(),
                pedido.getFechaCreacion(),
                pedido.getTotal(),
                detalles,
                pedido.getPago()
        );
    }

    public static Pedido toEntity(CrearPedidoDTO pedidoDTO, List<Producto> productos) {
        Objects.requireNonNull(pedidoDTO, "El DTO del pedido no puede ser nulo");

        List<DetallePedido> detalles = pedidoDTO.detallePedido().stream()
                .map(dto -> {
                    Producto producto = productos.stream()
                            .filter(p -> p.getIdProducto().equals(dto.idProducto()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Producto no encontrado: " + dto.idProducto()
                            ));

                    return new DetallePedido(
                            producto.getIdProducto(),
                            dto.cantidad(),
                            BigDecimal.valueOf(producto.getValor())
                    );
                })
                .toList();

        BigDecimal total = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoDTO.idCliente());
        pedido.setCodigoPasarela(pedidoDTO.codigoPasarela());
        pedido.setFechaCreacion(pedidoDTO.fechaCreacion());
        pedido.setDetalle(detalles);
        pedido.setPago(null); // inicialmente null
        pedido.setTotal(total);

        return pedido;
    }


    private static MostrarDetallePedidoDTO toMostrarDetallePedidoDTO(DetallePedido detalle, List<Producto> productos) {
        Producto producto = productos.stream()
                .filter(p -> p.getIdProducto().equals(detalle.getIdProducto()))
                .findFirst()
                .orElse(null);

        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return new MostrarDetallePedidoDTO(
                detalle.getIdProducto(),
                producto != null ? producto.getNombreProducto() : "Producto no disponible",
                producto != null ? producto.getTipo() : null,
                producto != null ? producto.getImagenProducto() : null,
                detalle.getPrecioUnitario(),
                detalle.getCantidad(),
                subtotal
        );
    }
}