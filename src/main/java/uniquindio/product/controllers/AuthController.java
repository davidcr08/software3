package uniquindio.product.controllers;

import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.autenticacion.TokenDTO;
import uniquindio.product.dto.usuario.*;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.services.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    //_______________________ENDPOINTS PARA AUTENTICACION_________________________________

    /**
     * Endpoint para crear una nueva cuenta de usuario.
     * @param usuarioDTO DTO con los datos del nuevo usuario
     * @return ResponseEntity con un mensaje indicando el resultado
     */
    @PostMapping("/crear-cuenta")
    public ResponseEntity<MensajeDTO<String>> crearCuenta(@Valid @RequestBody CrearUsuarioDTO usuarioDTO)
            throws UsuarioException, CarritoException, EmailException {
        usuarioService.crearUsuario(usuarioDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta creada correctamente"));
    }

    /**
     * Verifica el código de activación enviado al correo del usuario.
     * @param validarCodigoDTO DTO con correo y código de verificación
     * @return ResponseEntity con mensaje de activación
     * @throws UsuarioException si el código no es válido o expiró
     */
    @PostMapping("/validar-codigo")
    public ResponseEntity<MensajeDTO<String>> validarCodigo(@Valid @RequestBody ValidarCodigoDTO validarCodigoDTO)
            throws UsuarioException, EmailException {
        usuarioService.validarCodigo(validarCodigoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta activada con éxito"));
    }

    /**
     * Envía un código de recuperación de contraseña al correo del usuario.
     * @param codigoContraseniaDTO DTO con el email del usuario.
     * @return ResponseEntity con mensaje de confirmación.
     * @throws UsuarioException si no se encuentra la cuenta asociada.
     */
    @PostMapping("/codigo-recuperacion-contasenia")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacion(
            @Valid @RequestBody CodigoContraseniaDTO codigoContraseniaDTO) throws UsuarioException, EmailException {

        try {
            usuarioService.enviarCodigoRecuperacionPassword(codigoContraseniaDTO);

            return ResponseEntity.ok(new MensajeDTO<>(false, "Código enviado correctamente al correo electrónico."));

        } catch (UsuarioException e) {
            throw e;
        }
    }

    /**
     * Modifica la contraseña de un usuario o administrador a partir de un código de verificación.
     * @param cambiarPasswordDTO DTO con el código de verificación y la nueva contraseña.
     * @return ResponseEntity con mensaje de confirmación o error.
     * @throws UsuarioException si el código no es válido o no se encuentra la cuenta.
     */
    @PutMapping("/cambiar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws UsuarioException, EmailException {
            usuarioService.cambiarPassword(cambiarPasswordDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña modificada con éxito"));
    }

    //==================================== METODOS AUTENTICACION =============================================//

    /**
     * Inicia sesión de usuario y genera un token JWT.
     * @param loginDTO DTO con las credenciales de acceso.
     * @return ResponseEntity con el TokenDTO si las credenciales son válidas.
     * @throws UsuarioException si ocurre un error en la autenticación.
     */
    @PostMapping("/iniciar-sesion")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) throws UsuarioException {
        TokenDTO token = usuarioService.iniciarSesion(loginDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, token));
    }
}