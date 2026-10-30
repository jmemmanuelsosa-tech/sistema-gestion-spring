package com.sistema.gestion.controller;

import com.sistema.gestion.model.Producto;
import com.sistema.gestion.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarProductos(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("moduloActivo", "productos");
        return "productos"; 
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("producto", new Producto());
        model.addAttribute("moduloActivo", "productos");
        return "productos/producto_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("producto", productoService.buscarPorId(id));
        model.addAttribute("moduloActivo", "productos");
        return "productos/producto_form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes mensajero) {
        productoService.guardar(producto);
        mensajero.addFlashAttribute("mensajeExito", "Producto guardado correctamente.");
        return "redirect:/productos";
    }
}