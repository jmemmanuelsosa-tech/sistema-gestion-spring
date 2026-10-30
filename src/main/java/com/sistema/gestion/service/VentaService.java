package com.sistema.gestion.service;

import com.sistema.gestion.model.Venta;
import com.sistema.gestion.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    // Método principal que recibe el filtro
    public List<Venta> obtenerVentasFiltradas(String filtro) {
        LocalDateTime inicio;
        LocalDateTime fin = LocalDateTime.now().with(LocalTime.MAX);
        LocalDate hoy = LocalDate.now();

        if (filtro == null) filtro = "hoy"; // Por defecto muestra el cierre diario

        switch (filtro.toLowerCase()) {
            case "semana":
                inicio = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                break;
            case "mes":
                inicio = hoy.withDayOfMonth(1).atStartOfDay();
                break;
            case "ano":
                inicio = hoy.withDayOfYear(1).atStartOfDay();
                break;
            case "historico":
                return ventaRepository.findAll();
            case "hoy":
            default:
                inicio = hoy.atStartOfDay();
                break;
        }
        
        return ventaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);
    }

    public Double calcularTotal(List<Venta> ventas) {
        return ventas.stream().mapToDouble(Venta::getTotal).sum();
    }
}