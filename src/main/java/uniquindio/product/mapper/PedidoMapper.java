package uniquindio.product.mapper;

import uniquindio.product.dto.pedido.*;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetallePedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PedidoMapper {

    private PedidoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MostrarPedidoDTO toMostrarPedidoDTO(Pedido pedido, List<Producto> productos, List<Lote> lotes) {
        validatePedidoData(pedido, productos, lotes);

        List<MostrarDetallePedidoDTO> detalles = buildDetalles(pedido, productos, lotes);

        return new MostrarPedidoDTO(
                pedido.getId(),
                pedido.getIdCliente(),
                pedido.getFechaCreacion(),
                pedido.getTotal(),
                detalles
        );
    }

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido, List<Producto> productos, List<Lote> lotes) {
        validatePedidoData(pedido, productos, lotes);

        List<MostrarDetallePedidoDTO> detalles = buildDetalles(pedido, productos, lotes);

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getIdCliente(),
                pedido.getFechaCreacion(),
                pedido.getTotal(),
                detalles,
                pedido.getPago()
        );
    }


    public static Pedido toEntity(CrearPedidoDTO pedidoDTO, List<Producto> productos, Map<String, Lote> lotesAsignados) {
        Objects.requireNonNull(pedidoDTO, "El DTO del pedido no puede ser nulo");
        Objects.requireNonNull(productos, "La lista de productos no puede ser nula");
        Objects.requireNonNull(lotesAsignados, "Los lotes asignados no pueden ser nulos");

        Map<String, Producto> productoMap = productos.stream()
                .collect(Collectors.toMap(Producto::getIdProducto, p -> p));

        List<DetallePedido> detalles = pedidoDTO.detallePedido().stream()
                .map(dto -> {
                    Producto producto = productoMap.get(dto.idProducto());
                    if (producto == null) {
                        throw new IllegalArgumentException("Producto no encontrado: " + dto.idProducto());
                    }

                    Lote lote = lotesAsignados.get(dto.idProducto());
                    if (lote == null) {
                        throw new IllegalArgumentException("No se asignó lote al producto: " + dto.idProducto());
                    }

                    return new DetallePedido(
                            producto.getIdProducto(),
                            lote.getId(),
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
        pedido.setTotal(total);
        pedido.setPago(null);

        return pedido;
    }

    private static MostrarDetallePedidoDTO toMostrarDetallePedidoDTO(
            DetallePedido detalle,
            Map<String, Producto> productoMap,
            Map<String, Lote> loteMap
    ) {
        Producto producto = productoMap.get(detalle.getIdProducto());
        Lote lote = loteMap.get(detalle.getIdLote());

        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return new MostrarDetallePedidoDTO(
                detalle.getIdProducto(),
                producto != null ? producto.getNombreProducto() : "Producto no disponible",
                producto != null ? producto.getTipo() : null,
                producto != null ? producto.getImagenProducto() : null,
                detalle.getPrecioUnitario(),
                detalle.getCantidad(),
                subtotal,
                detalle.getIdLote(),
                lote != null ? lote.getCodigoLote() : null,
                lote != null ? lote.getFechaVencimiento() : null
        );
    }

// ============================
// Métodos privados auxiliares
// ============================

    private static void validatePedidoData(Pedido pedido, List<Producto> productos, List<Lote> lotes) {
        Objects.requireNonNull(pedido, "El pedido no puede ser nulo");
        Objects.requireNonNull(productos, "La lista de productos no puede ser nula");
        Objects.requireNonNull(lotes, "La lista de lotes no puede ser nula");

        if (pedido.getDetalle() == null || pedido.getDetalle().isEmpty()) {
            throw new IllegalArgumentException("El pedido no contiene detalles");
        }
    }

    private static List<MostrarDetallePedidoDTO> buildDetalles(
            Pedido pedido,
            List<Producto> productos,
            List<Lote> lotes
    ) {
        // Pre-construir maps para acceso O(1)
        Map<String, Producto> productoMap = productos.stream()
                .collect(Collectors.toMap(Producto::getIdProducto, p -> p));

        Map<String, Lote> loteMap = lotes.stream()
                .collect(Collectors.toMap(Lote::getId, l -> l));

        return pedido.getDetalle().stream()
                .map(detalle -> toMostrarDetallePedidoDTO(detalle, productoMap, loteMap))
                .toList();
    }
}