package com.sistema.gestion.controller;
import com.sistema.gestion.model.Cliente;
import com.sistema.gestion.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author sosap
 */
@Controller
public class ClienteWebController {
    private final ClienteService clienteService;

    public ClienteWebController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public String mostrarPantallaClientes(HttpSession session, Model model) {
        // Seguridad de navegacion: Si no inicio sesion, lo devuelve al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("listaClientes", clienteService.listarClientes());
        model.addAttribute("nuevoCliente", new Cliente());
        model.addAttribute("moduloActivo", "clientes"); // Marca el boton activo en el menu
        return "clientes";
    }

    @PostMapping("/clientes/guardar")
    public String guardarDesdeFormulario(@ModelAttribute("nuevoCliente") Cliente cliente,
                                         RedirectAttributes mensajero) {
        try {
            clienteService.guardarCliente(cliente);
            mensajero.addFlashAttribute("mensajeExito", "Cliente registrado correctamente en el sistema.");
        } catch (IllegalArgumentException ex) {
            mensajero.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarDesdePantalla(@PathVariable Long id, RedirectAttributes mensajero) {
        try {
            clienteService.eliminarCliente(id);
            mensajero.addFlashAttribute("mensajeExito", "El registro del cliente fue eliminado.");
        } catch (IllegalArgumentException ex) {
            mensajero.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/clientes";
    }
}
