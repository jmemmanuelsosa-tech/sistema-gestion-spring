package com.sistema.gestion.services;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 *
 * @author sosap
 */
@Service
public class UsuarioService {
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarParametrosSeguridad(usuario);

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Error: El nombre de usuario '" + usuario.getUsername() + "' ya está en uso.");
        }

        // Si no se especifica rol, asignamos uno por defecto seguro
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("VENDEDOR");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario validarLogin(String username, String passwordIngresada) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Error de acceso: Usuario no encontrado."));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("Cuenta bloqueada por seguridad. Contacte al administrador.");
        }

        // Comparacion directa temporal (sin hash aun, pero con control de intentos)
        if (!usuario.getPassword().equals(passwordIngresada)) {
            int nuevosIntentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(nuevosIntentos);

            if (nuevosIntentos >= MAX_INTENTOS_FALLIDOS) {
                usuario.setActivo(false);
            }
            usuarioRepository.save(usuario);
            throw new IllegalArgumentException("Contraseña incorrecta. Intento " + nuevosIntentos + " de " + MAX_INTENTOS_FALLIDOS);
        }

        // Si entra bien, reiniciamos el contador de fallos
        usuario.setIntentosFallidos(0);
        return usuarioRepository.save(usuario);
    }

    private void validarParametrosSeguridad(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().length() < 4) {
            throw new IllegalArgumentException("Seguridad: El username debe tener al menos 4 caracteres.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().length() < 8) {
            throw new IllegalArgumentException("Seguridad: La contraseña debe tener mínimo 8 caracteres.");
        }
        if (usuario.getPassword().contains(" ")) {
            throw new IllegalArgumentException("Seguridad: La contraseña no puede contener espacios en blanco.");
        }
    }
}
