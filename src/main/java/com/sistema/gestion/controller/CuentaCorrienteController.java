package com.sistema.gestion.controller;

import com.sistema.gestion.model.Cliente;
import com.sistema.gestion.model.Cobranza;
import com.sistema.gestion.services.ClienteService;
import com.sistema.gestion.services.CobranzaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cuenta-corriente")
public class CuentaCorrienteController {

    private final ClienteService clienteService;
    private final CobranzaService cobranzaService;

    public CuentaCorrienteController(ClienteService clienteService, CobranzaService cobranzaService) {
        this.clienteService = clienteService;
        this.cobranzaService = cobranzaService;
    }

    @GetMapping("/{clienteId}")
    public String verCuenta(@PathVariable Long clienteId, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        Cliente cliente = clienteService.buscarPorId(clienteId); 
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("historial", cobranzaService.obtenerHistorialPorCliente(clienteId));
        model.addAttribute("saldoActual", cobranzaService.calcularSaldoActual(clienteId));
        model.addAttribute("nuevaOperacion", new Cobranza());
        model.addAttribute("moduloActivo", "cobranzas"); 
        
        return "cuenta_corriente"; 
    }

    @PostMapping("/guardar")
    public String guardarOperacion(@ModelAttribute("nuevaOperacion") Cobranza cobranza, 
                                   @RequestParam Long clienteId,
                                   RedirectAttributes mensajero) {
        try {
            Cliente cliente = clienteService.buscarPorId(clienteId);
            cobranza.setCliente(cliente);
            cobranzaService.registrarOperacion(cobranza);
            
            mensajero.addFlashAttribute("mensajeExito", "Operación registrada correctamente.");
        } catch (Exception ex) {
            mensajero.addFlashAttribute("mensajeError", "Error: " + ex.getMessage());
        }
        
        return "redirect:/cuenta-corriente/" + clienteId;
    }
}