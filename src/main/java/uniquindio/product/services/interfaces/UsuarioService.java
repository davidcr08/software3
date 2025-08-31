package uniquindio.product.services.interfaces;

import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario) throws UsuarioException;
    Optional<Usuario> obtenerUsuario(String id) throws UsuarioException;
    Optional<Usuario> obtenerUsuarioPorCedula(String cedula) throws UsuarioException;
    Optional<Usuario> obtenerUsuarioPorEmail(String email) throws UsuarioException;
    Usuario actualizarUsuario(Usuario usuario) throws UsuarioException;
    void eliminarUsuario(String id) throws UsuarioException;
    List<Usuario> listarUsuarios();

    // Nuevos métodos - CORREGIDOS
    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws UsuarioException;
    void editarUsuario(EditarUsuarioDTO usuarioDTO) throws UsuarioException; // Este debe ser void

    void solicitarCambioContrasenia(CodigoContraseniaDTO codigoDTO) throws UsuarioException;

    void cambiarContrasenia(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException;

    void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException;
    void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException;
}