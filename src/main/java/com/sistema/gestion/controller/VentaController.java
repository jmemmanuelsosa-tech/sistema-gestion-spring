package com.sistema.gestion.controller;

import com.sistema.gestion.model.Venta;
import com.sistema.gestion.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sistema.gestion.service.ClienteService;
import com.sistema.gestion.service.ProductoService;
import com.sistema.gestion.dto.venta.VentaRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public VentaController(VentaService ventaService, ClienteService clienteService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    // Pantalla del Punto de Venta
    @GetMapping("/nueva")
    public String nuevaVenta(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("moduloActivo", "ventas");

        return "ventas/nueva_venta";
    }
    
    // Recibe el carrito mediante AJAX/Fetch desde Javascript
    @PostMapping("/api/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarVentaApi(@RequestBody VentaRequestDTO request) {
        try {
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito está vacío.");
            }
            if ("CUENTA_CORRIENTE".equals(request.getMetodoPago()) && request.getClienteId() == null) {
                return ResponseEntity.badRequest().body("Para vender a Cuenta Corriente debe seleccionar un cliente.");
            }
            
            // "Cajero" es un dato fijo por ahora, se cambiará por el usuario logueado después
            Venta ventaRegistrada = ventaService.registrarNuevaVenta(request, "Caja Principal");
            return ResponseEntity.ok(ventaRegistrada.getId());
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar venta: " + e.getMessage());
        }
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
    
    @GetMapping("/ticket/{id}")
    public String imprimirTicket(@PathVariable Long id, Model model) {
        model.addAttribute("venta", ventaService.obtenerVentaConDetalles(id));
        return "ventas/ticket";
    }
}