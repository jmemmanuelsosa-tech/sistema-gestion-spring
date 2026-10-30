package com.sistema.gestion.controller;

import com.sistema.gestion.model.Venta;
import com.sistema.gestion.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public String dashboardVentas(@RequestParam(required = false, defaultValue = "hoy") String filtro, 
                                  Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        List<Venta> ventasFiltradas = ventaService.obtenerVentasFiltradas(filtro);
        Double totalRecaudado = ventaService.calcularTotal(ventasFiltradas);

        model.addAttribute("ventas", ventasFiltradas);
        model.addAttribute("totalRecaudado", totalRecaudado);
        model.addAttribute("filtroActual", filtro);
        model.addAttribute("moduloActivo", "ventas");

        return "ventas/dashboard"; 
    }
}