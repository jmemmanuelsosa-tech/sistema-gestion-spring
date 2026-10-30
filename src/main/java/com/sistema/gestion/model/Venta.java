package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Puede ser nulo si es un "Consumidor Final" de paso
    @ManyToOne(optional = true)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_hora", updatable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Double total;

    // Ej: "EFECTIVO", "TRANSFERENCIA", "CUENTA_CORRIENTE"
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @PrePersist
    protected void alRegistrar() {
        this.fechaHora = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

}