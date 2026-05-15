package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Transaccion {
    private String trxId;
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private BigDecimal montoTrx;
    private BigDecimal comision;
    private String tipoTrx;
    private String estadoTrx;
    private String descripcionTrx;
    private Date fechaTrx;

    public void updateEstado(String estado){
        this.estadoTrx = estado;
    }

    // monto > 0
    public boolean hasValidMonto() {
        return montoTrx != null && montoTrx.compareTo(BigDecimal.ZERO) > 0;
    }

    // cuenta_origen_id ≠ cuenta_destino_id
    public boolean cuentasDistintas() {
        if (cuentaOrigen == null || cuentaDestino == null) return false;
        if (cuentaOrigen.getCuentaId() == null || cuentaDestino.getCuentaId() == null) return false;
        return !cuentaOrigen.getCuentaId().equals(cuentaDestino.getCuentaId());
    }

    // tipo: 'TRANSFERENCIA', 'DEPOSITO', 'RETIRO'
    public boolean hasValidTipo() {
        if (tipoTrx == null) return false;
        return tipoTrx.equals("TRANSFERENCIA") || tipoTrx.equals("DEPOSITO") || tipoTrx.equals("RETIRO");
    }

    // estado: 'PENDIENTE', 'COMPLETADA', 'FALLIDA', 'CANCELADA'
    public boolean hasValidEstado() {
        if (estadoTrx == null) return false;
        return estadoTrx.equals("PENDIENTE") || estadoTrx.equals("COMPLETADA")
                || estadoTrx.equals("FALLIDA") || estadoTrx.equals("CANCELADA");
    }
}
