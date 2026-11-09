package com.sistema.gestion.service;

import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarParametrosSeguridad(usuario);

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Error: El nombre de usuario '" + usuario.getUsername() + "' ya está en uso.");
        }

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("VENDEDOR");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
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