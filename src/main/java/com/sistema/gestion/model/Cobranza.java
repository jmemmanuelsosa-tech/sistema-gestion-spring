package com.sistema.gestion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cobranzas")
public class Cobranza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A quién le pertenece la deuda/pago
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // El nombre del vendedor físico que se elegirá en el desplegable
    @Column(nullable = false, length = 100)
    private String vendedor;

    // Guardaremos "DEUDA" o "PAGO"
    @Column(name = "tipo_operacion", nullable = false, length = 20)
    private String tipoOperacion; 

    @Column(nullable = false)
    private Double monto;

    @Column(length = 255)
    private String concepto;

    @Column(name = "fecha_operacion", updatable = false)
    private LocalDateTime fechaOperacion;

    public Cobranza() {
    }

    @PrePersist
    protected void alRegistrar() {
        this.fechaOperacion = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public LocalDateTime getFechaOperacion() { return fechaOperacion; }
}
