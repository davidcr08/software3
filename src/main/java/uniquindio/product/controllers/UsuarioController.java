package uniquindio.product.controllers;

import uniquindio.product.dto.usuario.CambiarPasswordDTO;
import uniquindio.product.dto.usuario.CodigoContraseniaDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearusuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/informacion/{id}")
    public ResponseEntity<InformacionUsuarioDTO> obtenerInformacionUsuario(@PathVariable String id) {
        try {
            InformacionUsuarioDTO informacion = usuarioService.obtenerInformacionUsuario(id);
            return ResponseEntity.ok(informacion);
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/editar")
    public ResponseEntity<Void> editarUsuario(@RequestBody EditarUsuarioDTO usuarioDTO) {
        try {
            usuarioService.editarUsuario(usuarioDTO);
            return ResponseEntity.ok().build();
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/recuperar-password")
    public ResponseEntity<Void> enviarCodigoRecuperacion(@RequestBody CodigoContraseniaDTO codigoDTO) {
        try {
            usuarioService.enviarCodigoRecuperacionPassword(codigoDTO);
            return ResponseEntity.ok().build();
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(@RequestBody CambiarPasswordDTO cambiarPasswordDTO) {
        try {
            usuarioService.cambiarPassword(cambiarPasswordDTO);
            return ResponseEntity.ok().build();
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerusuario(@PathVariable String id) {
        try {
            return usuarioService.obtenerUsuario(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Usuario> obtenerusuarioPorCedula(@PathVariable String cedula) {
        try {
            return usuarioService.obtenerUsuarioPorCedula(cedula)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerusuarioPorEmail(@PathVariable String email) {
        try {
            return usuarioService.obtenerUsuarioPorEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<Usuario> actualizarusuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarusuario(@PathVariable String id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException | UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarusuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}