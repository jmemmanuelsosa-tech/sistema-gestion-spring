package com.sistema.gestion.service;

import com.sistema.gestion.model.Venta;
import com.sistema.gestion.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistema.gestion.dto.venta.ItemCarritoDTO;
import com.sistema.gestion.dto.venta.VentaRequestDTO;
import com.sistema.gestion.model.Cliente;
import com.sistema.gestion.model.Cobranza;
import com.sistema.gestion.model.DetalleVenta;
import com.sistema.gestion.model.Producto;
import com.sistema.gestion.repository.ClienteRepository;
import com.sistema.gestion.repository.ProductoRepository;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CobranzaService cobranzaService;

    public VentaService(VentaRepository ventaRepository, ClienteRepository clienteRepository, 
                        ProductoRepository productoRepository, CobranzaService cobranzaService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.cobranzaService = cobranzaService;
    }
    

    // Método principal que recibe el filtro
    public List<Venta> obtenerVentasFiltradas(String filtro) {
        LocalDateTime inicio;
        LocalDateTime fin = LocalDateTime.now().with(LocalTime.MAX);
        LocalDate hoy = LocalDate.now();

        if (filtro == null) filtro = "hoy"; // Por defecto muestra el cierre diario

        switch (filtro.toLowerCase()) {
            case "semana":
                inicio = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                break;
            case "mes":
                inicio = hoy.withDayOfMonth(1).atStartOfDay();
                break;
            case "ano":
                inicio = hoy.withDayOfYear(1).atStartOfDay();
                break;
            case "historico":
                return ventaRepository.findAll();
            case "hoy":
            default:
                inicio = hoy.atStartOfDay();
                break;
        }
        
        return ventaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);
    }

    @Transactional
    public Venta registrarNuevaVenta(VentaRequestDTO request, String vendedorActivo) {
        Venta venta = new Venta();
        venta.setMetodoPago(request.getMetodoPago());
        
        Cliente cliente = null;
        if (request.getClienteId() != null) {
            cliente = clienteRepository.findById(request.getClienteId()).orElse(null);
            venta.setCliente(cliente);
        }

        double totalVenta = 0.0;

        for (ItemCarritoDTO itemDTO : request.getItems()) {
            Producto prod = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(prod);
            detalle.setCantidad(itemDTO.getCantidad());
            detalle.setPrecioUnitario(prod.getPrecio());
            
            if (prod.getStock() < itemDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para: " + prod.getNombre());
            }
            prod.setStock(prod.getStock() - itemDTO.getCantidad());
            
            double subtotal = prod.getPrecio() * itemDTO.getCantidad();
            detalle.setSubtotal(subtotal);
            totalVenta += subtotal;

            venta.getDetalles().add(detalle);
        }

        venta.setTotal(totalVenta);
        Venta ventaGuardada = ventaRepository.save(venta);

        // Si la venta es en cuenta corriente, generar deuda automáticamente
        if ("CUENTA_CORRIENTE".equalsIgnoreCase(request.getMetodoPago()) && cliente != null) {
            Cobranza deuda = new Cobranza();
            deuda.setCliente(cliente);
            deuda.setTipoOperacion("DEUDA");
            deuda.setMonto(totalVenta);
            deuda.setConcepto("Ref. Venta #" + ventaGuardada.getId());
            deuda.setVendedor(vendedorActivo); // Idealmente capturado de sesión, aquí pasamos un default o el logueado
            
            cobranzaService.registrarOperacion(deuda);
        }

        return ventaGuardada;
    }
    
    public Double calcularTotal(List<Venta> ventas) {
        return ventas.stream().mapToDouble(Venta::getTotal).sum();
    }
    
    @Transactional(readOnly = true)
    public Venta obtenerVentaConDetalles(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta != null) {
            venta.getDetalles().size(); 
        }
        return venta;
    }
}