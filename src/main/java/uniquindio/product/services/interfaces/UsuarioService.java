package uniquindio.product.services.interfaces;

import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.exceptions.UsuarioException;

import java.util.List;

public interface UsuarioService {

    InformacionUsuarioDTO obtenerUsuario(String id) throws UsuarioException;
    void crearUsuario(CrearUsuarioDTO crearUsuarioDTO) throws UsuarioException, CarritoException, EmailException;
    void editarUsuario(String id, EditarUsuarioDTO usuarioDTO) throws UsuarioException;
    void eliminarUsuario(String id) throws UsuarioException;
    void validarCodigo(ValidarCodigoDTO validarCodigoDTO) throws UsuarioException, EmailException;
    void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException, EmailException;
    void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException, EmailException;
    TokenDTO iniciarSesion(LoginDTO loginDTO) throws UsuarioException;
    void crearTrabajador(CrearTrabajadorDTO trabajadorDTO) throws UsuarioException;
    List<InformacionUsuarioDTO> listarTrabajadores() throws UsuarioException;
}