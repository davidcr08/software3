package uniquindio.product.services.implementations;

import uniquindio.product.enums.EstadoCuenta;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;

import uniquindio.product.repositories.UsuarioRepository;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario crearusuario(Usuario usuario) throws UsuarioException {
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new UsuarioException("Ya existe un usuario con la cédula: " + usuario.getCedula());
        }

        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new UsuarioException("Ya existe un usuario con el email: " + usuario.getCorreoElectronico());
        }

        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerusuario(String id) throws UsuarioException {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioException("No existe un usuario con el ID: " + id);
        }
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerusuarioPorCedula(String cedula) throws UsuarioException {
        if (!usuarioRepository.existsByCedula(cedula)) {
            throw new UsuarioException("No existe un usuario con la cédula: " + cedula);
        }
        return usuarioRepository.findByCedula(cedula);
    }

    @Override
    public Optional<Usuario> obtenerusuarioPorEmail(String email) throws UsuarioException {
        if (!usuarioRepository.existsByCorreoElectronico(email)) {
            throw new UsuarioException("No existe un usuario con el email: " + email);
        }
        return usuarioRepository.findByCorreoElectronico(email);
    }

    @Override
    public Usuario actualizarusuario(Usuario usuario) throws UsuarioException {
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new UsuarioException("No existe un usuario con el ID: " + usuario.getId());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarusuario(String id) throws UsuarioException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("No existe un usuario con el ID: " + id));
        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarusuarios() {
        return usuarioRepository.findAll();
    }
}