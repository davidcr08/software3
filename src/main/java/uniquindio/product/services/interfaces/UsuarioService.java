package uniquindio.product.services.interfaces;

import uniquindio.product.model.documents.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearusuario(Usuario usuario);
    Optional<Usuario> obtenerusuario(String id);
    Optional<Usuario> obtenerusuarioPorCedula(String cedula);
    Optional<Usuario> obtenerusuarioPorEmail(String email);
    Usuario actualizarusuario(Usuario usuario);
    void eliminarusuario(String id);
    List<Usuario> listarusuarios();
}