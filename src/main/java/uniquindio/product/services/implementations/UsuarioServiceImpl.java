package uniquindio.product.services.implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.carrito.CrearCarritoDTO;
import uniquindio.product.dto.email.EmailDTO;
import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.mapper.UsuarioMapper;
import uniquindio.product.model.enums.EstadoCuenta;
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
        return obtenerUsuario(
                () -> usuarioRepository.findById(id),
                "No existe un usuario con el ID: " + id
        );
    }

    /**
     * Método genérico para obtener usuario desde diferentes fuentes.
     * Reduce duplicación de código siguiendo el principio DRY.
     *
     * @param supplier función que proporciona el Optional<Usuario>
     * @param mensajeError mensaje de error personalizado si no se encuentra
     * @return InformacionUsuarioDTO con datos del usuario
     * @throws UsuarioException si no se encuentra el usuario
     */
    private InformacionUsuarioDTO obtenerUsuario(
            Supplier<Optional<Usuario>> supplier,
            String mensajeError
    ) throws UsuarioException {

        Usuario usuario = supplier.get()
                .orElseThrow(() -> new UsuarioException(mensajeError));

        return UsuarioMapper.toInformacionUsuarioDTO(usuario);
    }

    /**
     * Crea un nuevo usuario (CLIENTE) con carrito asociado.
     * El usuario se crea en estado INACTIVO y requiere validación por email.
     *
     * @param usuarioDTO datos del usuario a crear
     * @throws UsuarioException si ya existe usuario con email o cédula
     * @throws CarritoException si ocurre un error en la creación del carrito
     * @throws EmailException si ocurre un error al enviar el email
     */
    @Override
    @Transactional
    public void crearUsuario(CrearUsuarioDTO usuarioDTO)
            throws UsuarioException, CarritoException, EmailException {

        validarUsuarioNoExiste(usuarioDTO.correoElectronico(), usuarioDTO.cedula());

        Usuario usuario = UsuarioMapper.toUsuarioEntity(usuarioDTO, passwordEncoder);

        String codigoValidacion = codigoValidacionService.generarCodigoValidacion(5);
        UsuarioMapper.configurarCodigoValidacion(usuario, codigoValidacion, 10L);

        Usuario usuarioCreado = usuarioRepository.save(usuario);

        CrearCarritoDTO carritoDTO = UsuarioMapper.toCrearCarritoDTO(usuarioCreado.getId());
        carritoService.crearCarrito(carritoDTO);

        EmailDTO emailDTO = UsuarioMapper.toEmailValidacionDTO(
                usuarioDTO.correoElectronico(),
                codigoValidacion
        );
        emailService.enviarCorreo(emailDTO);
    }

    /**
     * Valida que no exista un usuario con el email o cédula proporcionados.
     * Método privado reutilizable para validaciones de duplicados.
     *
     * @param correoElectronico email a validar
     * @param cedula cédula a validar
     * @throws UsuarioException si existe duplicado
     */
    private void validarUsuarioNoExiste(String correoElectronico, String cedula)
            throws UsuarioException {

        if (usuarioRepository.existsByCorreoElectronico(correoElectronico)) {
            throw new UsuarioException("Ya existe un usuario con este correo electrónico.");
        }

        if (usuarioRepository.existsByCedula(cedula)) {
            throw new UsuarioException("Ya existe un usuario con esta cédula.");
        }
    }

    /**
     * Valida el código de activación enviado al correo del usuario.
     * Activa la cuenta si el código es correcto y no ha expirado.
     *
     * @param validarCodigoDTO DTO con correo y código de verificación
     * @throws UsuarioException si el código es inválido, incorrecto o ha expirado
     * @throws EmailException   si ocurre un error al enviar nuevo código
     */
    @Override
    @Transactional
    public void validarCodigo(ValidarCodigoDTO validarCodigoDTO)
            throws UsuarioException, EmailException {

        Usuario usuario = usuarioRepository.findByCorreoElectronico(validarCodigoDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException(
                        "No se encontró un usuario con este correo electrónico."
                ));

        if (UsuarioMapper.codigoHaExpirado(usuario)) {
            manejarCodigoExpirado(usuario, usuario.getCorreoElectronico(), "Recuperación");
            throw new UsuarioException(
                    "El código de recuperación ha expirado. Se ha enviado un nuevo código."
            );
        }

        if (!UsuarioMapper.codigoEsValido(usuario, validarCodigoDTO.codigo())) {
            throw new UsuarioException("El código de verificación es incorrecto.");
        }

        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);

        usuarioRepository.save(usuario);

    }

    /**
     * Edita los datos de un usuario existente.
     * Permite actualizar nombre, teléfono y opcionalmente la contraseña.
     *
     * @param usuarioEditado DTO con los datos a actualizar
     * @throws UsuarioException si no existe un usuario con el ID dado
     */
    @Override
    @Transactional
    public void editarUsuario(String id, EditarUsuarioDTO usuarioEditado) throws UsuarioException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el id dado"));

        UsuarioMapper.actualizarDatosUsuario(usuario, usuarioEditado);

        UsuarioMapper.actualizarContrasena(usuario, usuarioEditado.contrasenia(), passwordEncoder);

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
     * Envía un código de recuperación de contraseña al correo del usuario.
     * Genera un código temporal válido por 10 minutos.
     *
     * @param codigoContraseniaDTO DTO con el correo electrónico del usuario
     * @throws UsuarioException si no se encuentra el usuario o la cuenta está inactiva
     * @throws EmailException si ocurre un error al enviar el email
     */
    @Override
    @Transactional
    public void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO)
            throws UsuarioException, EmailException {

        Usuario usuario = usuarioRepository.findByCorreoElectronico(codigoContraseniaDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException(
                        "Usuario no encontrado para el correo proporcionado"
                ));

        if (UsuarioMapper.cuentaEstaActiva(usuario)) {
            throw new UsuarioException("La cuenta no está activa. Active su cuenta primero.");
        }

        String codigoRecuperacion = codigoValidacionService.generarCodigoValidacion(5);

        UsuarioMapper.configurarCodigoValidacion(usuario, codigoRecuperacion, 10L);

        usuarioRepository.save(usuario);

        EmailDTO emailDTO = UsuarioMapper.toEmailRecuperacionDTO(
                codigoContraseniaDTO.correoElectronico(),
                codigoRecuperacion
        );
        emailService.enviarCorreo(emailDTO);
    }

    /**
     * Cambia la contraseña de un usuario utilizando un código de verificación.
     * El código debe ser válido y no estar expirado.
     * Después del cambio, se limpia el código de verificación.
     *
     * @param cambiarPasswordDTO DTO con el código y la nueva contraseña
     * @throws UsuarioException si el código es inválido, incorrecto o ha expirado
     * @throws EmailException si ocurre un error al enviar nuevo código (cuando expira)
     */
    @Override
    @Transactional
    public void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO)
            throws UsuarioException, EmailException {

        Usuario usuario = usuarioRepository.findByCodigoVerificacionContrasenia(
                        cambiarPasswordDTO.codigoVerificacion()
                )
                .orElseThrow(() -> new UsuarioException(
                        "Código de verificación inválido o expirado."
                ));

        if (UsuarioMapper.codigoHaExpirado(usuario)) {
            manejarCodigoExpirado(usuario, usuario.getCorreoElectronico(), "Recuperación");
            throw new UsuarioException(
                    "El código de recuperación ha expirado. Se ha enviado un nuevo código."
            );
        }

        UsuarioMapper.cambiarPasswordYLimpiarCodigo(
                usuario,
                cambiarPasswordDTO.passwordNueva(),
                passwordEncoder
        );

        usuarioRepository.save(usuario);
    }

    /**
     * Inicia sesión de un usuario y genera un token JWT.
     * Valida credenciales y estado de la cuenta antes de generar el token.
     *
     * @param loginDTO DTO con correo electrónico y contraseña
     * @return TokenDTO con el JWT generado
     * @throws UsuarioException si el usuario no existe, la contraseña es incorrecta o la cuenta está inactiva
     */
    @Override
    public TokenDTO iniciarSesion(LoginDTO loginDTO) throws UsuarioException {

        Usuario usuario = usuarioRepository.findByCorreoElectronico(loginDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("Correo electrónico o contraseña incorrectos."));

        if (!UsuarioMapper.credencialesSonValidas(loginDTO.password(), usuario.getContrasena(), passwordEncoder)) {
            throw new UsuarioException("La contraseña es incorrecta.");
        }

        if (!UsuarioMapper.cuentaEstaActiva(usuario)) {
            throw new UsuarioException("La cuenta no está activa.");
        }

        Map<String, Object> claims = UsuarioMapper.construirClaimsJWT(usuario);

        String tokenJWT = jwtUtils.generarToken(usuario.getCorreoElectronico(), claims);
        return UsuarioMapper.toTokenDTO(tokenJWT);
    }

    /**
     * Maneja el caso de código expirado generando y enviando uno nuevo.
     * Método privado reutilizable para cualquier tipo de código.
     *
     * @param usuario entidad Usuario a actualizar
     * @param correo correo electrónico del usuario
     * @param tipo tipo de código ("Validación", "Recuperación", etc.)
     * @throws EmailException si ocurre un error al enviar el email
     */
    private void manejarCodigoExpirado(Usuario usuario, String correo, String tipo)
            throws EmailException {

        String nuevoCodigo = codigoValidacionService.generarCodigoValidacion(5);

        UsuarioMapper.configurarCodigoValidacion(usuario, nuevoCodigo, 10L);

        usuarioRepository.save(usuario);

        EmailDTO emailDTO = UsuarioMapper.toEmailCodigoExpiradoDTO(correo, nuevoCodigo, tipo);
        emailService.enviarCorreo(emailDTO);
    }

    /**
     * Crea un trabajador (empleado) sin carrito asociado.
     * Solo accesible por ADMINISTRADOR.
     * La cuenta se crea directamente ACTIVA (sin código de validación).
     *
     * @param trabajadorDTO datos del trabajador a crear
     * @throws UsuarioException si ya existe usuario con email o cédula, o si intenta crear CLIENTE
     */
    @Override
    @Transactional
    public void crearTrabajador(CrearTrabajadorDTO trabajadorDTO) throws UsuarioException {

        validarUsuarioNoExiste(trabajadorDTO.correoElectronico(), trabajadorDTO.cedula());

        if (!UsuarioMapper.esRolDeTrabajador(trabajadorDTO.rol())) {
            throw new UsuarioException(
                    "No se puede crear un CLIENTE desde este endpoint. Use el endpoint público de registro."
            );
        }

        Usuario trabajador = UsuarioMapper.toTrabajadorEntity(trabajadorDTO, passwordEncoder);

        usuarioRepository.save(trabajador);
    }

    /**
     * Lista todos los usuarios que NO son CLIENTE (trabajadores).
     *
     * @return lista de trabajadores como InformacionUsuarioDTO
     */
    @Override
    public List<InformacionUsuarioDTO> listarTrabajadores() {
        List<Usuario> todosLosUsuarios = usuarioRepository.findAll();

        List<Usuario> trabajadores = UsuarioMapper.filtrarTrabajadores(todosLosUsuarios);

        return UsuarioMapper.toInformacionUsuarioDTOList(trabajadores);
    }
}