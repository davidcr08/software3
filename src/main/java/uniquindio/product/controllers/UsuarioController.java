package uniquindio.product.controllers;

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
            Usuario usuarioCreado = usuarioService.crearusuario(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerusuario(@PathVariable String id) {
        try {
            return usuarioService.obtenerusuario(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Usuario> obtenerusuarioPorCedula(@PathVariable String cedula) {
        try {
            return usuarioService.obtenerusuarioPorCedula(cedula)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerusuarioPorEmail(@PathVariable String email) {
        try {
            return usuarioService.obtenerusuarioPorEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<Usuario> actualizarusuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarusuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarusuario(@PathVariable String id) {
        try {
            usuarioService.eliminarusuario(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarusuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarusuarios();
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}