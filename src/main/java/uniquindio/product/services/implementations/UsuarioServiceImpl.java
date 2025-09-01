package uniquindio.product.services.implementations;

import uniquindio.product.model.enums.EstadoCuenta;
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
    public Usuario crearusuario(Usuario usuario) {
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new RuntimeException("Ya existe un usuario con esa cédula");
        }

        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerusuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No existe un usuario con ese ID");
        }
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerusuarioPorCedula(String cedula) {
        if (!usuarioRepository.existsByCedula(cedula)) {
            throw new RuntimeException("No existe un usuario con esa cédula");
        }
        return usuarioRepository.findByCedula(cedula);
    }

    @Override
    public Optional<Usuario> obtenerusuarioPorEmail(String email) {
        if (!usuarioRepository.existsByCorreoElectronico(email)) {
            throw new RuntimeException("No existe un usuario con ese email");
        }
        return usuarioRepository.findByCorreoElectronico(email);
    }

    @Override
    public Usuario actualizarusuario(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new RuntimeException("No existe un usuario con ese ID");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarusuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese ID"));
        usuario.setEstadoCuenta(EstadoCuenta.INACTIVO);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarusuarios() {
        return usuarioRepository.findAll();
    }
}