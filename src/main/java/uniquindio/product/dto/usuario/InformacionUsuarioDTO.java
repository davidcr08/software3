package uniquindio.product.dto.usuario;

import uniquindio.product.model.enums.Rol;

public record InformacionUsuarioDTO(

        String cedula,
        String nombre,
        String telefono,
        String correoElectronico,
        Rol rol
) {}