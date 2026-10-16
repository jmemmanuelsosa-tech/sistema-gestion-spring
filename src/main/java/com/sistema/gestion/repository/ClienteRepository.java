package com.sistema.gestion.repository;
import com.sistema.gestion.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author sosap
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long>{
    // Sirve para verificar si ya existe un email
    boolean existsByEmail(String email);

    // Sirve para verificar si ya existe un DNI
    boolean existsByDni(String dni);
}
