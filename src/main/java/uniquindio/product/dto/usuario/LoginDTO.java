package uniquindio.product.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "El correo electrónico es requerido")
        @Email(message = "El correo electrónico no es válido")
        String correoElectronico,

        @NotBlank(message = "La contraseña es requerida")
        String password
) {}