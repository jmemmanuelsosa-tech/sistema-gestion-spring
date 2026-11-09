package com.sistema.gestion.controller;

import com.sistema.gestion.dto.reporte.DeudorDTO;
import com.sistema.gestion.model.Cliente;
import com.sistema.gestion.service.ClienteService;
import com.sistema.gestion.service.CobranzaService;
import com.sistema.gestion.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ClienteService clienteService;
    private final CobranzaService cobranzaService;
    private final ProductoService productoService;

    public ReporteController(ClienteService clienteService, CobranzaService cobranzaService, ProductoService productoService) {
        this.clienteService = clienteService;
        this.cobranzaService = cobranzaService;
        this.productoService = productoService;
    }

    @GetMapping
    public String verReportes(Model model, HttpSession session) {

        List<Cliente> todosLosClientes = clienteService.listarClientes();
        List<DeudorDTO> listaDeudores = new ArrayList<>();
        double totalDeudaEnLaCalle = 0.0;

        // Calculamos quién debe plata y sumamos el total global
        for (Cliente c : todosLosClientes) {
            Double saldo = cobranzaService.calcularSaldoActual(c.getId());
            if (saldo > 0) {
                listaDeudores.add(new DeudorDTO(c, saldo));
                totalDeudaEnLaCalle += saldo;
            }
        }

        // Ordenamos la lista para que los que más deben aparezcan arriba (Top Morosos)
        listaDeudores.sort((d1, d2) -> d2.getSaldoDeudor().compareTo(d1.getSaldoDeudor()));

        model.addAttribute("totalClientes", todosLosClientes.size());
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalDeuda", totalDeudaEnLaCalle);
        model.addAttribute("listaDeudores", listaDeudores);
        model.addAttribute("moduloActivo", "reportes");

        return "reportes/dashboard_reportes";
    }
}
