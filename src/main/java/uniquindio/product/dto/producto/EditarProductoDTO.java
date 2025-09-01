package uniquindio.product.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uniquindio.product.model.enums.TipoProducto;

public record EditarProductoDTO(

        @NotBlank(message = "La imagen del producto es obligatoria.")
        String imagenProducto,

        @NotNull(message = "La cantidad es obligatoria.")
        @Positive(message = "La cantidad debe ser mayor a 0.")
        Integer cantidad,

        @NotNull(message = "El valor es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a 0.")
        Double valor,

        @NotNull(message = "El tipo de producto es obligatorio.")
        TipoProducto tipo

) {}