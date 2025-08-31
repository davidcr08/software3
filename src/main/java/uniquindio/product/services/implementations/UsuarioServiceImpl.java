package uniquindio.product.services.implementations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uniquindio.product.dto.usuario.CambiarPasswordDTO;
import uniquindio.product.dto.usuario.CodigoContraseniaDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.enums.EstadoCuenta;
import uniquindio.product.enums.Rol;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.repositories.UsuarioRepository;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor


    public class UsuarioServiceImpl implements UsuarioService {

        private final UsuarioRepository usuarioRepository;
        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        @Override
        public Usuario crearUsuario(Usuario usuario) throws UsuarioException {
            if (usuarioRepository.existsByCedula(usuario.getCedula())) {
                throw new UsuarioException("Ya existe un usuario con la cédula: " + usuario.getCedula());
            }

            if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
                throw new UsuarioException("Ya existe un usuario con el email: " + usuario.getCorreoElectronico());
            }

            usuario.setRol(Rol.CLIENTE);
            usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);

            // Encriptar contraseña
            if (usuario.getContrasena() != null) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }

            return usuarioRepository.save(usuario);
        }

    @Override
    public Optional<Usuario> obtenerUsuario(String id) throws UsuarioException {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioException("No existe un usuario con el ID: " + id);
        }
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCedula(String cedula) throws UsuarioException {
        if (!usuarioRepository.existsByCedula(cedula)) {
            throw new UsuarioException("No existe un usuario con la cédula: " + cedula);
        }
        return usuarioRepository.findByCedula(cedula);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) throws UsuarioException {
        if (!usuarioRepository.existsByCorreoElectronico(email)) {
            throw new UsuarioException("No existe un usuario con el email: " + email);
        }
        return usuarioRepository.findByCorreoElectronico(email);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws UsuarioException {
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new UsuarioException("No existe un usuario con el ID: " + usuario.getId());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(String id) throws UsuarioException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el ID: " + id));
        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws UsuarioException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el ID: " + id));

        return new InformacionUsuarioDTO(
                usuario.getId(),
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getCorreoElectronico()
        );
    }

    @Override
    public void editarUsuario(EditarUsuarioDTO usuarioDTO) throws UsuarioException {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.id())
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el ID: " + usuarioDTO.id()));

        usuario.setNombre(usuarioDTO.nombre());
        usuario.setTelefono(usuarioDTO.telefono());

        // Actualizar contraseña si se proporciona
        if (usuarioDTO.contrasena() != null && !usuarioDTO.contrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.contrasena()));
        }

        usuarioRepository.save(usuario);
    }

    @Override
    public void solicitarCambioContrasenia(CodigoContraseniaDTO codigoDTO) throws UsuarioException {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(codigoDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el email: " + codigoDTO.correoElectronico()));

        // Aquí generarías y guardarías un código de verificación
        // Por ahora simulamos la generación de código
        String codigoVerificacion = UUID.randomUUID().toString().substring(0, 6);
        System.out.println("Código de verificación para " + usuario.getCorreoElectronico() + ": " + codigoVerificacion);

        // En una implementación real, enviarías este código por email
    }

    @Override
    public void cambiarContrasenia(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException {

        System.out.println("Cambiando contraseña con código: " + cambiarPasswordDTO.codigoVerificacion());

    }
    @Override
    public void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(codigoContraseniaDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el email: " + codigoContraseniaDTO.correoElectronico()));

        // Generar código de verificación
        String codigoVerificacion = generarCodigoVerificacion();
        usuario.setCodigoVerificacionContrasenia(codigoVerificacion);
        usuario.setFechaExpiracionCodigoContrasenia(LocalDateTime.now().plusMinutes(10));

        usuarioRepository.save(usuario);

        // Aquí deberías implementar el envío de email (similar al proyecto de referencia)
        System.out.println("Código de verificación para " + usuario.getCorreoElectronico() + ": " + codigoVerificacion);
    }

    @Override
    public void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException {
        Usuario usuario = usuarioRepository.findByCodigoVerificacionContrasenia(cambiarPasswordDTO.codigoVerificacion())
                .orElseThrow(() -> new UsuarioException("Código de verificación inválido"));

        // Verificar si el código ha expirado
        if (usuario.getFechaExpiracionCodigoContrasenia() != null &&
                LocalDateTime.now().isAfter(usuario.getFechaExpiracionCodigoContrasenia())) {
            throw new UsuarioException("El código de verificación ha expirado");
        }

        // Cambiar la contraseña
        usuario.setContrasena(passwordEncoder.encode(cambiarPasswordDTO.passwordNueva()));
        usuario.setCodigoVerificacionContrasenia(null);
        usuario.setFechaExpiracionCodigoContrasenia(null);

        usuarioRepository.save(usuario);
    }

    private String generarCodigoVerificacion() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}