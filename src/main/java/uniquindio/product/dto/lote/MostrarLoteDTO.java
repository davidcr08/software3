package uniquindio.product.dto.lote;

import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.enums.EstadoLote;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record MostrarLoteDTO(
        String id,
        String codigoLote,
        String idProducto,
        String nombreProducto,
        LocalDate fechaProduccion,
        LocalDate fechaVencimiento,
        Integer cantidadProducida,
        Integer cantidadDisponible,
        Integer cantidadVendida,
        EstadoLote estado,
        String observaciones,

        boolean proximoAVencer,
        long diasParaVencer
) {
    // Constructor con cálculos
    public MostrarLoteDTO(Lote lote, String nombreProducto) {
        this(
                lote.getId(),
                lote.getCodigoLote(),
                lote.getIdProducto(),
                nombreProducto,
                lote.getFechaProduccion(),
                lote.getFechaVencimiento(),
                lote.getCantidadProducida(),
                lote.getCantidadDisponible(),
                lote.getCantidadProducida() - lote.getCantidadDisponible(),
                lote.getEstado(),
                lote.getObservaciones(),
                lote.diasParaVencer() < 60,
                lote.diasParaVencer()
        );
    }
}