package uniquindio.product.mapper;

import uniquindio.product.dto.inventario.ProductoBajoStockDTO;
import uniquindio.product.dto.inventario.ResumenInventarioDTO;
import uniquindio.product.dto.inventario.StockPorLoteDTO;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.repositories.ProductoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class InventarioMapper {

    private InventarioMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<ResumenInventarioDTO> toResumenInventarioDTOList(
            Map<String, List<Lote>> lotesPorProducto,
            ProductoRepository productoRepository
    ) {
        // Obtener todos los productos de una sola vez
        List<String> idsProductos = new ArrayList<>(lotesPorProducto.keySet());
        List<Producto> productos = productoRepository.findAllById(idsProductos);

        // Crear mapa para acceso rápido por ID
        Map<String, String> nombresPorId = productos.stream()
                .collect(Collectors.toMap(
                        Producto::getIdProducto,
                        Producto::getNombreProducto
                ));

        return lotesPorProducto.entrySet().stream()
                .map(entry -> mapToResumenInventarioDTO(
                        entry.getKey(),
                        entry.getValue(),
                        nombresPorId
                ))
                .toList();
    }

    private static ResumenInventarioDTO mapToResumenInventarioDTO(
            String idProducto,
            List<Lote> lotes,
            Map<String, String> nombresPorId
    ) {
        int stockTotal = lotes.stream()
                .mapToInt(Lote::getCantidadDisponible)
                .sum();

        int lotesDisponibles = lotes.size();

        LocalDate proximoVencimiento = lotes.stream()
                .map(Lote::getFechaVencimiento)
                .min(LocalDate::compareTo)
                .orElse(null);

        String nombreProducto = nombresPorId.getOrDefault(idProducto, "Desconocido");

        return new ResumenInventarioDTO(
                idProducto,
                nombreProducto,
                stockTotal,
                lotesDisponibles,
                proximoVencimiento
        );
    }

    public static List<StockPorLoteDTO> toStockPorLoteDTOList(List<Lote> lotes) {
        return lotes.stream()
                .map(InventarioMapper::toStockPorLoteDTO)
                .toList();
    }

    private static StockPorLoteDTO toStockPorLoteDTO(Lote lote) {
        return new StockPorLoteDTO(
                lote.getCodigoLote(),
                lote.getCantidadDisponible(),
                lote.getFechaVencimiento(),
                lote.getEstado()
        );
    }

    public static ProductoBajoStockDTO toProductoBajoStockDTO(Producto producto, int stockTotal, int umbral) {
        return new ProductoBajoStockDTO(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                stockTotal,
                umbral
        );
    }
}