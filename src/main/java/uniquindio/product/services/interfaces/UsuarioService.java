package uniquindio.product.services.interfaces;

import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearusuario(Usuario usuario) throws UsuarioException;
    Optional<Usuario> obtenerusuario(String id) throws UsuarioException;
    Optional<Usuario> obtenerusuarioPorCedula(String cedula) throws UsuarioException;
    Optional<Usuario> obtenerusuarioPorEmail(String email) throws UsuarioException;
    Usuario actualizarusuario(Usuario usuario) throws UsuarioException;
    void eliminarusuario(String id) throws UsuarioException;
    List<Usuario> listarusuarios();
}