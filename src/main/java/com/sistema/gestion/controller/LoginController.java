package com.sistema.gestion.controller;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author sosap
 */
@Controller
public class LoginController {
private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/inicio";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        try {
            Usuario usuario = usuarioService.validarLogin(username, password);
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/inicio";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("errorLogin", ex.getMessage());
            model.addAttribute("usernameIngresado", username);
            return "login";
        }
    }

    // Procesa el formulario de registro del Modal en el Login
    @PostMapping("/registro-rapido")
    public String registrarDesdeLogin(@RequestParam String newUsername, 
                                      @RequestParam String newPassword, 
                                      RedirectAttributes mensajero) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setUsername(newUsername);
            nuevo.setPassword(newPassword);
            nuevo.setRol("Vendedor"); // Por defecto le damos rol Vendedor por seguridad
            nuevo.setActivo(true);
            
            usuarioService.registrarUsuario(nuevo);
            mensajero.addFlashAttribute("mensajeExito", "¡Cuenta creada! Ya puedes iniciar sesión.");
        } catch (Exception ex) {
            mensajero.addFlashAttribute("errorLogin", "Error al registrar: " + ex.getMessage());
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes mensajero) {
        session.invalidate();
        mensajero.addFlashAttribute("mensajeLogout", "Has cerrado sesión correctamente.");
        return "redirect:/login";
    }

    // Pantalla principal del sistema (Dashboard)
    @GetMapping({"/", "/inicio"})
    public String mostrarInicio(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        model.addAttribute("moduloActivo", "inicio");
        return "inicio"; 
    }
}
