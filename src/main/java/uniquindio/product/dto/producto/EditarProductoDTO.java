package uniquindio.product.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import uniquindio.product.model.enums.TipoProducto;

public record EditarProductoDTO(

        @NotBlank(message = "El nombre es requerido")
        @Length(max = 100, message = "El nombre debe tener un máximo de 100 caracteres")
        String nombre,

        @NotBlank(message = "La imagen del producto es obligatoria.")
        String imagenProducto,

        @NotBlank(message = "El nombre es requerido")
        @Length(max = 500, min = 30, message = "La descripcion del producto debe tener un máximo de 500 caracteres")
        String descripcion,

        @NotNull(message = "El valor es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a 0.")
        Double valor,

        @NotNull(message = "El tipo de producto es obligatorio.")
        TipoProducto tipo

) {}