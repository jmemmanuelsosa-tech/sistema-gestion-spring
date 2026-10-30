package com.sistema.gestion.service;

import com.sistema.gestion.model.Cobranza;
import com.sistema.gestion.repository.CobranzaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CobranzaService {

    private final CobranzaRepository cobranzaRepository;

    public CobranzaService(CobranzaRepository cobranzaRepository) {
        this.cobranzaRepository = cobranzaRepository;
    }

    public Cobranza registrarOperacion(Cobranza cobranza) {
        if (cobranza.getMonto() == null || cobranza.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (cobranza.getTipoOperacion() == null || cobranza.getTipoOperacion().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar si es DEUDA o PAGO.");
        }
        if (cobranza.getVendedor() == null || cobranza.getVendedor().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar el vendedor que atiende.");
        }
        return cobranzaRepository.save(cobranza);
    }

    public List<Cobranza> obtenerHistorialPorCliente(Long clienteId) {
        return cobranzaRepository.findByClienteIdOrderByFechaOperacionDesc(clienteId);
    }

    public Double calcularSaldoActual(Long clienteId) {
        List<Cobranza> historial = obtenerHistorialPorCliente(clienteId);
        
        double totalDeuda = 0.0;
        double totalPagos = 0.0;

        for (Cobranza cob : historial) {
            if ("DEUDA".equalsIgnoreCase(cob.getTipoOperacion())) {
                totalDeuda += cob.getMonto();
            } else if ("PAGO".equalsIgnoreCase(cob.getTipoOperacion())) {
                totalPagos += cob.getMonto();
            }
        }
        return totalDeuda - totalPagos;
    }
}