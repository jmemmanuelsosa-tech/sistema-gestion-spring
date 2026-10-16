package com.sistema.gestion.controller;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *
 * @author sosap
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
