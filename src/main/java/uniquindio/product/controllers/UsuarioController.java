package uniquindio.product.controllers;

import org.springframework.security.core.Authentication;
import uniquindio.product.configs.AuthUtils;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.usuario.CambiarPasswordDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.exceptions.EmailException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<MensajeDTO<InformacionUsuarioDTO>> obtenerInformacionUsuario(Authentication authentication) throws UsuarioException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        InformacionUsuarioDTO informacion = usuarioService.obtenerUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(true, informacion));
    }
    @PutMapping("/editar")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(@RequestBody EditarUsuarioDTO usuarioDTO) {
        try {
            usuarioService.editarUsuario(usuarioDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Perfil actualizado correctamente"));
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<MensajeDTO<String>> eliminarUsuario(Authentication authentication) throws UsuarioException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }
}