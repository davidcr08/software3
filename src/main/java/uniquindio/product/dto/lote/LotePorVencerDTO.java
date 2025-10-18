package uniquindio.product.dto.lote;

import java.time.LocalDate;

public record LotePorVencerDTO(
        String codigoLote,
        String nombreProducto,
        LocalDate fechaVencimiento,
        Integer cantidadDisponible,
        long diasRestantes
) {}