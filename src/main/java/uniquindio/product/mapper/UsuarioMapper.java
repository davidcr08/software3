package uniquindio.product.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.carrito.CrearCarritoDTO;
import uniquindio.product.dto.email.EmailDTO;
import uniquindio.product.dto.usuario.CrearTrabajadorDTO;
import uniquindio.product.dto.usuario.CrearUsuarioDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.model.enums.EstadoCuenta;
import uniquindio.product.model.enums.Rol;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper para transformaciones de Usuario a DTOs.
 * Centraliza toda la lógica de mapeo siguiendo principios de Clean Code.
 *
 * @author Tu nombre
 * @version 1.0
 */
public final class UsuarioMapper {

    /**
     * Constructor privado para evitar instanciación.
     * Esta es una clase de utilidad con métodos estáticos únicamente.
     */
    private UsuarioMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Mapea una entidad Usuario a InformacionUsuarioDTO.
     *
     * @param usuario entidad Usuario a mapear
     * @return DTO con la información básica del usuario
     */
    public static InformacionUsuarioDTO toInformacionUsuarioDTO(Usuario usuario) {
        return new InformacionUsuarioDTO(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getCorreoElectronico(),
                usuario.getRol(),
                usuario.getCiudadDeResidencia(),
                usuario.getDireccion()
        );
    }

    /**
     * Mapea una lista de entidades Usuario a lista de InformacionUsuarioDTO.
     * Útil para operaciones que devuelven múltiples usuarios.
     *
     * @param usuarios lista de entidades Usuario
     * @return lista de DTOs con información de usuarios
     */
    public static List<InformacionUsuarioDTO> toInformacionUsuarioDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toInformacionUsuarioDTO)
                .toList();
    }

    /**
     * Crea una entidad Usuario a partir de CrearUsuarioDTO.
     * Configura valores por defecto para un CLIENTE nuevo (INACTIVO, sin validar).
     *
     * @param usuarioDTO DTO con datos del usuario a crear
     * @param passwordEncoder encoder para encriptar la contraseña
     * @return entidad Usuario lista para persistir (sin ID)
     */
    public static Usuario toUsuarioEntity(CrearUsuarioDTO usuarioDTO, PasswordEncoder passwordEncoder) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setCedula(usuarioDTO.cedula());
        usuario.setTelefono(usuarioDTO.telefono());
        usuario.setCorreoElectronico(usuarioDTO.correoElectronico());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.contrasenia()));
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);
        return usuario;
    }

    /**
     * Configura el código de validación y su fecha de expiración en un usuario.
     * Utilizado para activación de cuenta y recuperación de contraseña.
     *
     * @param usuario entidad Usuario a configurar
     * @param codigoValidacion código generado
     * @param minutosExpiracion minutos hasta que expire el código
     */
    public static void configurarCodigoValidacion(Usuario usuario, String codigoValidacion, long minutosExpiracion) {
        usuario.setCodigoVerificacionContrasenia(codigoValidacion);
        usuario.setFechaExpiracionCodigoContrasenia(LocalDateTime.now().plusMinutes(minutosExpiracion));
    }

    /**
     * Crea un CrearCarritoDTO a partir del ID de usuario.
     *
     * @param idUsuario ID del usuario propietario del carrito
     * @return DTO para crear carrito vacío
     */
    public static CrearCarritoDTO toCrearCarritoDTO(String idUsuario) {
        return new CrearCarritoDTO(idUsuario, new ArrayList<>());
    }

    /**
     * Crea un EmailDTO para envío de código de validación.
     *
     * @param correoDestino email del destinatario
     * @param codigoValidacion código a enviar
     * @return DTO con el email configurado
     */
    public static EmailDTO toEmailValidacionDTO(String correoDestino, String codigoValidacion) {
        String asunto = "Código de Validación";
        String cuerpo = "Tu código de validación es: " + codigoValidacion;
        return new EmailDTO(asunto, cuerpo, correoDestino);
    }

    /**
     * Limpia el código de validación y su fecha de expiración.
     * Utilizado después de validar exitosamente un código.
     *
     * @param usuario entidad Usuario a limpiar
     */
    public static void limpiarCodigoValidacion(Usuario usuario) {
        usuario.setCodigoVerificacionContrasenia(null);
        usuario.setFechaExpiracionCodigoContrasenia(null);
    }

    /**
     * Verifica si el código de validación de un usuario ha expirado.
     *
     * @param usuario entidad Usuario a verificar
     * @return true si el código ha expirado, false en caso contrario
     */
    public static boolean codigoHaExpirado(Usuario usuario) {
        return usuario.getFechaExpiracionCodigoContrasenia() != null &&
                LocalDateTime.now().isAfter(usuario.getFechaExpiracionCodigoContrasenia());
    }

    /**
     * Verifica si el código proporcionado coincide con el código almacenado.
     *
     * @param usuario entidad Usuario con el código almacenado
     * @param codigoIngresado código ingresado por el usuario
     * @return true si los códigos coinciden, false en caso contrario
     */
    public static boolean codigoEsValido(Usuario usuario, String codigoIngresado) {
        return usuario.getCodigoVerificacionContrasenia() != null &&
                usuario.getCodigoVerificacionContrasenia().equals(codigoIngresado);
    }

    /**
     * Actualiza los datos editables de un usuario desde EditarUsuarioDTO.
     * La contraseña se maneja por separado.
     *
     * @param usuario entidad Usuario a actualizar
     * @param usuarioEditado DTO con los nuevos datos
     */
    public static void actualizarDatosUsuario(Usuario usuario, EditarUsuarioDTO usuarioEditado) {
        usuario.setNombre(usuarioEditado.nombre());
        usuario.setTelefono(usuarioEditado.telefono());
        usuario.setCiudadDeResidencia(usuarioEditado.ciudadDeResidencia());
        usuario.setDireccion(usuarioEditado.direccion());
    }

    /**
     * Actualiza la contraseña de un usuario si fue proporcionada.
     * Verifica que la contraseña no sea nula ni vacía antes de encriptar.
     *
     * @param usuario entidad Usuario a actualizar
     * @param contrasenaNueva contraseña en texto plano
     * @param passwordEncoder encoder para encriptar la contraseña
     */
    public static void actualizarContrasena(Usuario usuario, String contrasenaNueva, PasswordEncoder passwordEncoder) {
        if (contrasenaNueva != null && !contrasenaNueva.isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
        }
    }

    /**
     * Crea un EmailDTO para envío de código de recuperación de contraseña.
     *
     * @param correoDestino email del destinatario
     * @param codigoRecuperacion código generado
     * @return DTO con el email configurado para recuperación
     */
    public static EmailDTO toEmailRecuperacionDTO(String correoDestino, String codigoRecuperacion) {
        String asunto = "Código de recuperación de contraseña";
        String cuerpo = "El código para recuperar tu contraseña es: " + codigoRecuperacion;
        return new EmailDTO(asunto, cuerpo, correoDestino);
    }

    /**
     * Verifica si una cuenta de usuario está activa.
     *
     * @param usuario entidad Usuario a verificar
     * @return true si la cuenta está activa, false en caso contrario
     */
    public static boolean cuentaEstaActiva(Usuario usuario) {
        return EstadoCuenta.ACTIVO.equals(usuario.getEstadoCuenta());
    }

    /**
     * Cambia la contraseña de un usuario y limpia los códigos de verificación.
     *
     * @param usuario entidad Usuario a actualizar
     * @param passwordNueva nueva contraseña en texto plano
     * @param passwordEncoder encoder para encriptar la contraseña
     */
    public static void cambiarPasswordYLimpiarCodigo(Usuario usuario, String passwordNueva, PasswordEncoder passwordEncoder) {
        usuario.setContrasena(passwordEncoder.encode(passwordNueva));
        limpiarCodigoValidacion(usuario);
    }

    /**
     * Construye el mapa de claims (datos) para incluir en el JWT.
     * Incluye rol, nombre e ID del usuario.
     *
     * @param usuario entidad Usuario desde la cual extraer los claims
     * @return mapa con los claims del JWT
     */
    public static Map<String, Object> construirClaimsJWT(Usuario usuario) {
        return Map.of(
                "rol", usuario.getRol(),
                "nombre", usuario.getNombre(),
                "id", usuario.getId()
        );
    }

    /**
     * Crea un TokenDTO a partir del token JWT generado.
     *
     * @param tokenJWT token JWT en formato String
     * @return DTO con el token encapsulado
     */
    public static TokenDTO toTokenDTO(String tokenJWT) {
        return new TokenDTO(tokenJWT);
    }

    /**
     * Valida las credenciales del usuario contra la contraseña encriptada.
     *
     * @param passwordIngresada contraseña en texto plano ingresada
     * @param passwordEncriptada contraseña encriptada almacenada en BD
     * @param passwordEncoder encoder para comparar contraseñas
     * @return true si las contraseñas coinciden, false en caso contrario
     */
    public static boolean credencialesSonValidas(String passwordIngresada, String passwordEncriptada, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(passwordIngresada, passwordEncriptada);
    }

    /**
     * Crea una entidad Usuario (trabajador) a partir de CrearTrabajadorDTO.
     * La cuenta se crea directamente ACTIVA (sin validación por email).
     *
     * @param trabajadorDTO DTO con datos del trabajador a crear
     * @param passwordEncoder encoder para encriptar la contraseña
     * @return entidad Usuario lista para persistir (sin ID)
     */
    public static Usuario toTrabajadorEntity(CrearTrabajadorDTO trabajadorDTO, PasswordEncoder passwordEncoder) {
        Usuario trabajador = new Usuario();
        trabajador.setNombre(trabajadorDTO.nombre());
        trabajador.setCedula(trabajadorDTO.cedula());
        trabajador.setTelefono(trabajadorDTO.telefono());
        trabajador.setCorreoElectronico(trabajadorDTO.correoElectronico());
        trabajador.setContrasena(passwordEncoder.encode(trabajadorDTO.contrasenia()));
        trabajador.setRol(trabajadorDTO.rol());
        trabajador.setEstadoCuenta(EstadoCuenta.ACTIVO);

        // Campos opcionales
        trabajador.setCiudadDeResidencia(trabajadorDTO.ciudadDeResidencia());
        trabajador.setDireccion(trabajadorDTO.direccion());

        return trabajador;
    }

    /**
     * Crea un EmailDTO para reenvío de código cuando expira.
     *
     * @param correoDestino email del destinatario
     * @param nuevoCodigo código regenerado
     * @param tipo tipo de código ("Validación", "Recuperación", etc.)
     * @return DTO con el email configurado
     */
    public static EmailDTO toEmailCodigoExpiradoDTO(String correoDestino, String nuevoCodigo, String tipo) {
        String asunto = "Nuevo Código de " + tipo;
        String cuerpo = "Tu nuevo código de " + tipo.toLowerCase() + " es: " + nuevoCodigo;
        return new EmailDTO(asunto, cuerpo, correoDestino);
    }

    /**
     * Filtra una lista de usuarios para obtener solo trabajadores (no CLIENTE).
     *
     * @param usuarios lista completa de usuarios
     * @return lista filtrada con solo trabajadores
     */
    public static List<Usuario> filtrarTrabajadores(List<Usuario> usuarios) {
        return usuarios.stream()
                .filter(usuario -> usuario.getRol() != Rol.CLIENTE)
                .toList();
    }

    /**
     * Verifica si un rol es válido para crear trabajador (no CLIENTE).
     *
     * @param rol rol a validar
     * @return true si es un rol de trabajador, false si es CLIENTE
     */
    public static boolean esRolDeTrabajador(Rol rol) {
        return rol != Rol.CLIENTE;
    }

}