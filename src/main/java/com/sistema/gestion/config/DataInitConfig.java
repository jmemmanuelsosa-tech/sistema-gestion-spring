package com.sistema.gestion.config;

import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); 
                admin.setRol("ADMIN");
                admin.setActivo(true);
                usuarioRepository.save(admin);
            }
        };
    }
}