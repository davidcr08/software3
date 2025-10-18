package uniquindio.product.dto.lote;

import jakarta.validation.constraints.NotNull;

public record AjustarCantidadLoteDTO(

        @NotNull(message = "El ID del lote es obligatorio")
        String idLote,

        @NotNull(message = "La cantidad de ajuste es obligatoria")
        Integer ajuste
) {}