package com.sistema.gestion.dto.reporte;

import com.sistema.gestion.model.Cliente;

public class DeudorDTO {
    private Cliente cliente;
    private Double saldoDeudor;

    public DeudorDTO(Cliente cliente, Double saldoDeudor) {
        this.cliente = cliente;
        this.saldoDeudor = saldoDeudor;
    }

    public Cliente getCliente() { 
        return cliente; 
    }
    
    public Double getSaldoDeudor() { 
        return saldoDeudor; 
    }
    
}