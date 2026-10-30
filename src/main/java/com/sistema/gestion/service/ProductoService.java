package com.sistema.gestion.service;

import com.sistema.gestion.model.Producto;
import com.sistema.gestion.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() { return productoRepository.findAll(); }
    
    public Producto guardar(Producto producto) { return productoRepository.save(producto); }
    
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }
    
    public void eliminar(Long id) { productoRepository.deleteById(id); }
}