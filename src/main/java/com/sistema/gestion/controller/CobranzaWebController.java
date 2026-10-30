package com.sistema.gestion.controller;

import com.sistema.gestion.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CobranzaWebController {

    private final ClienteService clienteService;

    public CobranzaWebController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/cobranzas")
    public String inicioCobranzas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        // Traemos a todos los clientes para que el cajero elija a quién cobrarle
        model.addAttribute("listaClientes", clienteService.listarClientes());
        model.addAttribute("moduloActivo", "cobranzas"); // Ilumina el botón Cobranzas
        
        return "cobranzas";
    }
}