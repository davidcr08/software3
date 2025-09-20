package uniquindio.product.services.interfaces;

import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsuarioService {

    void crearUsuario(CrearUsuarioDTO crearUsuarioDTO) throws UsuarioException, CarritoException, EmailException;
    InformacionUsuarioDTO obtenerUsuario(String id) throws UsuarioException;
    InformacionUsuarioDTO obtenerUsuarioPorCedula(String cedula) throws UsuarioException;
    InformacionUsuarioDTO obtenerUsuarioPorCorreo(String correoElectronico) throws UsuarioException;
    void editarUsuario(EditarUsuarioDTO usuarioDTO) throws UsuarioException;
    void eliminarUsuario(String id) throws UsuarioException;
    ValidarCodigoDTO validarCodigo(ValidarCodigoDTO validarCodigoDTO) throws UsuarioException, EmailException;
    void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException, EmailException;
    void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException, EmailException;
    TokenDTO iniciarSesion(LoginDTO loginDTO) throws UsuarioException;
    Map<String, Object> construirClaims(Usuario usuario);
}

