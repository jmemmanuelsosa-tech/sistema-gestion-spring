package com.sistema.gestion.repository;

import com.sistema.gestion.model.Cobranza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CobranzaRepository extends JpaRepository<Cobranza, Long> {

    // Spring Boot crea la consulta SQL sola para traer el historial ordenado por fecha
    List<Cobranza> findByClienteIdOrderByFechaOperacionDesc(Long clienteId);
}
