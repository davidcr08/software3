package uniquindio.product.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CodigoContraseniaDTO(
        @NotBlank(message = "El correo electrónico es requerido")
        @Length(max = 50, message = "El correo electrónico debe tener un máximo de 50 caracteres")
        @Email(message = "El correo electrónico no es válido")
        String correoElectronico
) {}