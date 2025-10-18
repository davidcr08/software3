package uniquindio.product.dto.inventario;

import uniquindio.product.model.enums.EstadoLote;

import java.time.LocalDate;

public record StockPorLoteDTO(
        String codigoLote,
        Integer cantidad,
        LocalDate fechaVencimiento,
        EstadoLote estado
) {}