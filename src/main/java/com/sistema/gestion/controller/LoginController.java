package com.sistema.gestion.controller;

import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/registro-rapido")
    public String registrarDesdeLogin(@RequestParam String newUsername,
                                      @RequestParam String newPassword,
                                      RedirectAttributes mensajero) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setUsername(newUsername);
            nuevo.setPassword(newPassword);
            nuevo.setRol("VENDEDOR");
            nuevo.setActivo(true);

            usuarioService.registrarUsuario(nuevo);
            mensajero.addFlashAttribute("mensajeExito", "¡Cuenta creada! Ya puedes iniciar sesión.");
        } catch (Exception ex) {
            mensajero.addFlashAttribute("errorLogin", "Error al registrar: " + ex.getMessage());
        }
        return "redirect:/login";
    }

    @GetMapping({"/", "/inicio"})
    public String mostrarInicio(Model model) {
        model.addAttribute("moduloActivo", "inicio");
        return "inicio";
    }
}