package uniquindio.product.dto.usuario;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import uniquindio.product.model.enums.Rol;

public record CrearTrabajadorDTO(

        @NotBlank(message = "La cédula es requerida")
        @Length(min = 7, max = 11, message = "La cédula debe tener un mínimo de 7 y máximo de 11 caracteres")
        @Pattern(regexp = "\\d+", message = "La cédula solo debe contener números")
        String cedula,

        @NotBlank(message = "El nombre es requerido")
        @Length(max = 100, message = "El nombre debe tener un máximo de 100 caracteres")
        String nombre,

        @NotBlank(message = "El teléfono es requerido")
        @Length(max = 10, message = "El teléfono debe tener un máximo de 10 caracteres")
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos")
        String telefono,

        @NotBlank(message = "El email es requerido")
        @Length(max = 50, message = "El email debe tener un máximo de 50 caracteres")
        @Email(message = "El email no es válido")
        String correoElectronico,

        @NotBlank(message = "La contraseña es requerida")
        @Length(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        String contrasenia,

        @NotNull(message = "El rol es requerido")
        Rol rol,

        @Length(max = 100, message = "La ciudad debe tener un máximo de 100 caracteres")
        String ciudadDeResidencia,

        @Length(max = 200, message = "La dirección debe tener un máximo de 200 caracteres")
        String direccion
) {
    /**
     * Validación personalizada: solo permite crear trabajadores (no CLIENTE).
     */
    public CrearTrabajadorDTO {
        if (rol == Rol.CLIENTE) {
            throw new IllegalArgumentException("No se puede crear un CLIENTE desde este endpoint. Use /api/auth/crear-cuenta");
        }
    }
}