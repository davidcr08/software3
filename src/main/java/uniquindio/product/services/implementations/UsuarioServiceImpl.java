package uniquindio.product.services.implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.carrito.CrearCarritoDTO;
import uniquindio.product.dto.email.EmailDTO;
import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.model.enums.EstadoCuenta;
import uniquindio.product.model.enums.Rol;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.repositories.UsuarioRepository;
import uniquindio.product.services.interfaces.CarritoService;
import uniquindio.product.services.interfaces.CodigoValidacionService;
import uniquindio.product.services.interfaces.EmailService;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import uniquindio.product.configs.JWTUtils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarritoService carritoService;
    private final EmailService emailService;
    private final CodigoValidacionService codigoValidacionService;
    private final JWTUtils jwtUtils;

    @Override
    public InformacionUsuarioDTO obtenerUsuario(String id) throws UsuarioException {
        return obtenerUsuario(() -> usuarioRepository.findById(id), "No existe un usuario con el ID: " + id);
    }

    @Override
    public InformacionUsuarioDTO obtenerUsuarioPorCedula(String cedula) throws UsuarioException {
        return obtenerUsuario(() -> usuarioRepository.findByCedula(cedula), "No existe un usuario con la cédula: " + cedula);
    }

    @Override
    public InformacionUsuarioDTO obtenerUsuarioPorCorreo(String correoElectronico) throws UsuarioException {
        return obtenerUsuario(() -> usuarioRepository.findByCorreoElectronico(correoElectronico), "No existe un usuario con el correo: " + correoElectronico);
    }

    private InformacionUsuarioDTO obtenerUsuario(Supplier<Optional<Usuario>> supplier, String mensajeError) throws UsuarioException {
        Usuario usuario = supplier.get().orElseThrow(() -> new UsuarioException(mensajeError));
        return mapearAInformacionUsuarioDTO(usuario);
    }


    private InformacionUsuarioDTO mapearAInformacionUsuarioDTO(Usuario usuario) {
        return new InformacionUsuarioDTO(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getCorreoElectronico()
        );
    }

    /**
     * Método para crear un nuevo usuario y su carrito asociado.
     * @param usuarioDTO datos del usuario a crear.
     * @throws UsuarioException si ya existe usuario con email o cédula.
     * @throws CarritoException si ocurre un error en la creación del carrito.
     */
    @Override
    @Transactional
    public void crearUsuario(CrearUsuarioDTO usuarioDTO) throws UsuarioException, CarritoException, EmailException {

        // Verificar duplicados
        if (usuarioRepository.existsByCorreoElectronico(usuarioDTO.correoElectronico())) {
            throw new UsuarioException("Ya existe un usuario con este correo electrónico.");
        }
        if (usuarioRepository.existsByCedula(usuarioDTO.cedula())) {
            throw new UsuarioException("Ya existe un usuario con esta cédula.");
        }

        // Crear entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setCedula(usuarioDTO.cedula());
        usuario.setTelefono(usuarioDTO.telefono());
        usuario.setCorreoElectronico(usuarioDTO.correoElectronico());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.contrasenia()));
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);

        // Generar código de validación
        String codigoValidacion = codigoValidacionService.generarCodigoValidacion(5);
        usuario.setCodigoVerificacionContrasenia(codigoValidacion);
        usuario.setFechaExpiracionCodigoContrasenia(LocalDateTime.now().plusMinutes(10));

        // Guardar usuario
        Usuario usuarioCreado = usuarioRepository.save(usuario);

        // Crear carrito asociado
        CrearCarritoDTO carritoDTO = new CrearCarritoDTO(
                usuarioCreado.getId(),
                new ArrayList<>()
        );
        carritoService.crearCarrito(carritoDTO);

        // Enviar correo de validación
        String asunto = "Código de Validación";
        String cuerpo = "Tu código de validación es: " + codigoValidacion;
        EmailDTO emailDTO = new EmailDTO(asunto, cuerpo, usuarioDTO.correoElectronico());
        emailService.enviarCorreo(emailDTO);
    }

    @Override
    @Transactional
    public ValidarCodigoDTO validarCodigo(ValidarCodigoDTO validarCodigoDTO) throws UsuarioException, EmailException {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByCorreoElectronico(validarCodigoDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("No se encontró un usuario con este correo electrónico."));

        // Validar si el código ha expirado
        if (usuario.getFechaExpiracionCodigoContrasenia() != null &&
                LocalDateTime.now().isAfter(usuario.getFechaExpiracionCodigoContrasenia())) {

            manejarCodigoExpirado(usuario, usuario.getCorreoElectronico(), "Recuperación");
            throw new UsuarioException("El código de recuperación ha expirado. Se ha enviado un nuevo código.");
        }

        // Comparar el código ingresado con el almacenado
        if (!usuario.getCodigoVerificacionContrasenia().equals(validarCodigoDTO.codigo())) {
            throw new UsuarioException("El código de verificación es incorrecto.");
        }

        // Si el código es correcto y no ha expirado → activar cuenta
        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
        usuario.setCodigoVerificacionContrasenia(null);
        usuario.setFechaExpiracionCodigoContrasenia(null);
        usuarioRepository.save(usuario);

        return validarCodigoDTO;
    }

    /**
     * Método para editar los datos de un usuario
     * @param usuarioEditado DTO con los datos a actualizar
     * @throws UsuarioException si no existe un usuario con el ID dado
     */
    @Override
    @Transactional
    public void editarUsuario(EditarUsuarioDTO usuarioEditado) throws UsuarioException {
        // Buscar usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioEditado.id())
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el id dado"));

        // Actualizar campos editables
        usuario.setNombre(usuarioEditado.nombre());
        usuario.setTelefono(usuarioEditado.telefono());

        // Actualizar contraseña solo si fue proporcionada
        if (usuarioEditado.contrasenia() != null && !usuarioEditado.contrasenia().isEmpty()) {
            String encriptada = passwordEncoder.encode(usuarioEditado.contrasenia());
            usuario.setContrasena(encriptada);
        }

        // Guardar cambios (al tener ID, save() actualiza en lugar de crear)
        usuarioRepository.save(usuario);
    }

    /**
     * Metodo para eliminar una cuenta por su ID
     * param id
     * return el id de la cuenta eliminada
     * throws CuentaException
     */
    @Override
    @Transactional
    public void eliminarUsuario(String id) throws UsuarioException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el ID: " + id));

        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);
        usuarioRepository.save(usuario);
    }

    /**
     * Metodo para enviar el código de recuperación de contraseña a un usuario
     * @param codigoContraseniaDTO DTO con el correo electrónico del usuario
     * @throws UsuarioException si no se encuentra el usuario
     */
    @Override
    public void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException, EmailException {

        // Buscar el usuario en la base de datos usando el correo proporcionado
        Usuario usuarioExistente = usuarioRepository.findByCorreoElectronico(codigoContraseniaDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("Usuario no encontrado para el correo proporcionado"));

        // Generar un código de recuperación único
        String codigoRecuperacion = codigoValidacionService.generarCodigoValidacion(5);

        // Guardar el código y la fecha de expiración
        usuarioExistente.setCodigoVerificacionContrasenia(codigoRecuperacion);
        usuarioExistente.setFechaExpiracionCodigoContrasenia(LocalDateTime.now().plusMinutes(10));
        usuarioRepository.save(usuarioExistente);

        // Enviar el código al correo electrónico
        String asunto = "Código de recuperación de contraseña";
        String cuerpo = "El código para recuperar tu contraseña es: " + codigoRecuperacion;
        EmailDTO emailDTO = new EmailDTO(asunto, cuerpo, codigoContraseniaDTO.correoElectronico());
        emailService.enviarCorreo(emailDTO);
    }

    /**
     * Método para cambiar la contraseña de un usuario a partir de un código de verificación
     * @param cambiarPasswordDTO DTO con el código y la nueva contraseña
     * @throws UsuarioException si el código no es válido o ha expirado
     */
    @Override
    public void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException, EmailException {
        // Buscar al usuario por el código de verificación
        Usuario usuario = usuarioRepository.findByCodigoVerificacionContrasenia(cambiarPasswordDTO.codigoVerificacion())
                .orElseThrow(() -> new UsuarioException("Código de verificación inválido o expirado."));

        // Validar si el código ha expirado
        if (usuario.getFechaExpiracionCodigoContrasenia() != null &&
                LocalDateTime.now().isAfter(usuario.getFechaExpiracionCodigoContrasenia())) {

            manejarCodigoExpirado(usuario, usuario.getCorreoElectronico(), "Recuperación");
            throw new UsuarioException("El código de recuperación ha expirado. Se ha enviado un nuevo código.");
        }

        // Si el código aún es válido → encriptar la nueva contraseña
        String contrasenaEncriptada = passwordEncoder.encode(cambiarPasswordDTO.passwordNueva());
        usuario.setContrasena(contrasenaEncriptada);

        // Limpiar código de verificación y expiración
        usuario.setCodigoVerificacionContrasenia(null);
        usuario.setFechaExpiracionCodigoContrasenia(null);

        usuarioRepository.save(usuario);
    }

    /**
     * Método para iniciar sesión de un usuario
     * @param loginDTO DTO con correo electrónico y contraseña
     * @return TokenDTO con el JWT generado
     * @throws UsuarioException si el usuario no existe, la contraseña es incorrecta o la cuenta está inactiva
     */
    @Override
    public TokenDTO iniciarSesion(LoginDTO loginDTO) throws UsuarioException {
        // Buscar el usuario por correo electrónico
        Usuario usuario = usuarioRepository.findByCorreoElectronico(loginDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("Correo electrónico o contraseña incorrectos."));

        // Validar contraseña
        if (!passwordEncoder.matches(loginDTO.password(), usuario.getContrasena())) {
            throw new UsuarioException("La contraseña es incorrecta.");
        }

        // Verificar estado de la cuenta
        if (!usuario.getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new UsuarioException("La cuenta no está activa.");
        }

        // Construir claims para el JWT
        Map<String, Object> claims = construirClaims(usuario);

        // Retornar el token generado
        return new TokenDTO(jwtUtils.generarToken(usuario.getCorreoElectronico(), claims));
    }

    @Override
    public Map<String, Object> construirClaims(Usuario usuario) {
        return Map.of(
                "rol", usuario.getRol(),
                "nombre", usuario.getNombre(),
                "id", usuario.getId()
        );
    }

    private void manejarCodigoExpirado(Usuario usuario, String correo, String tipo) throws EmailException {
        String nuevoCodigo = codigoValidacionService.generarCodigoValidacion(5);
        usuario.setCodigoVerificacionContrasenia(nuevoCodigo);
        usuario.setFechaExpiracionCodigoContrasenia(LocalDateTime.now().plusMinutes(10));
        usuarioRepository.save(usuario);

        String asunto = "Nuevo Código de " + tipo;
        String cuerpo = "Tu nuevo código de " + tipo.toLowerCase() + " es: " + nuevoCodigo;
        emailService.enviarCorreo(new EmailDTO(asunto, cuerpo, correo));
    }
}