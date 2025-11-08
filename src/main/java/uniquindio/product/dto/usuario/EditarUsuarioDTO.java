package uniquindio.product.dto.usuario;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record EditarUsuarioDTO(

        @Length(min= 10,max = 50, message = "El nombre debe tener un minimo de 10 y un máximo de 50 caracteres")
        String nombre,

        @Length(min= 10, max = 10, message = "El teléfono debe tener un máximo de 10 caracteres")
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener solo números")
        String telefono,

        @Length(min = 7, max = 20, message = "La contraseña debe tener un mínimo de 7 caracteres y un máximo de 20 caracteres")
        String contrasenia,

        @Length(max = 100, message = "La ciudad de residencia no puede superar los 100 caracteres")
        String ciudadDeResidencia,

        @Length(max = 150, message = "La dirección no puede superar los 150 caracteres")
        String direccion
) {}