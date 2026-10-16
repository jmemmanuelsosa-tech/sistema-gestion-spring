package com.sistema.gestion.repository;
import com.sistema.gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author sosap
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring crea la consulta SQL automaticamente leyendo el nombre del metodo
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
