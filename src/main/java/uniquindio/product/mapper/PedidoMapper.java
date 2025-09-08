package uniquindio.product.mapper;

import uniquindio.product.dto.pedido.*;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;

import java.math.BigDecimal;
import java.util.List;

public class PedidoMapper {

    public static MostrarPedidoDTO toMostrarPedidoDTO(Pedido pedido, String nombreCliente, List<Producto> productos) {
        List<MostrarDetallePedidoDTO> detalles = pedido.getDetalle().stream()
                .map(detalle -> mapearDetalle(detalle, productos))
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
        // Reutilizamos el overload, pasando el idCliente como "nombre"
        return toMostrarPedidoDTO(pedido, pedido.getIdCliente(), productos);
    }

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido, List<Producto> productos) {
        List<MostrarDetallePedidoDTO> detalles = pedido.getDetalle().stream()
                .map(detalle -> mapearDetalle(detalle, productos))
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

    private static MostrarDetallePedidoDTO mapearDetalle(DetallePedido detalle, List<Producto> productos) {
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
    public static Pedido toEntity(CrearPedidoDTO pedidoDTO, List<Producto> productos) {
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
                            BigDecimal.valueOf(producto.getValor()) // ✅ Double → BigDecimal
                    );
                })
                .toList();

        PagoDTO pagoDTO = pedidoDTO.pago();
        Pago pago = new Pago(
                pagoDTO.idPago(),
                pagoDTO.moneda(),
                pagoDTO.tipoPago(),
                pagoDTO.detalleEstado(),
                pagoDTO.codigoAutorizacion(),
                pagoDTO.fecha(),
                pagoDTO.valorTransaccion(),
                pagoDTO.estado(),
                pagoDTO.metodoPago()
        );

        // Calcular total del pedido
        BigDecimal total = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoDTO.idCliente());
        pedido.setCodigoPasarela(pedidoDTO.codigoPasarela());
        pedido.setFechaCreacion(pedidoDTO.fechaCreacion());
        pedido.setDetalle(detalles);
        pedido.setPago(pago);
        pedido.setTotal(total);

        return pedido;
    }

}
