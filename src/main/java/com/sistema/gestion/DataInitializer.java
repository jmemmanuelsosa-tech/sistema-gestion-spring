package com.sistema.gestion;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author sosap
 */
@Component
public class DataInitializer implements CommandLineRunner{
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void run(String... args) {
        // Verifica si existe el usuario 'admin'. Si no existe, lo crea automaticamente.
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin1234"); // Cumple los 8 caracteres minimos de seguridad
            admin.setRol("Administrador");
            admin.setActivo(true);
            admin.setIntentosFallidos(0);

            usuarioRepository.save(admin);
            System.out.println("--> [SISTEMA INICIADO]: Usuario por defecto creado");
            System.out.println("--> Usuario: admin | Contraseña: admin1234");
        }
    }
}
