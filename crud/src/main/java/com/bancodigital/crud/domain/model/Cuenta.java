package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Cuenta {
    private String cuentaId;
    private Cliente cliente;
    private String numCuenta;
    private BigDecimal saldo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    public Cuenta() {
        this.cuentaId = cuentaId;
        this.numCuenta = numCuenta;
        this.estado = estado;
    }


    public void updateCuenta(BigDecimal saldo){
        this.saldo = saldo;
    }
    // --- Validaciones de dominio ---

    public boolean hasValidSaldo() {
        return saldo != null && saldo.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean hasValidEstado() {
        return estado != null &&
                List.of("ACTIVO", "CERRADO").contains(estado.toUpperCase());
    }

    public boolean hasValidNumCuenta() {
        return numCuenta != null && !numCuenta.trim().isEmpty();
    }
}
