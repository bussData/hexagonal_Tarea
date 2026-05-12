package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Transaccion {
    private String trxId;
    private String cuentaOrigenId;
    private String cuentaDestinoId;
    private double montoTrx;
    private double comision;
    private String tipoTrx;
    private String estadoTrx;
    private String descripcionTrx;
    private Date fechaTrx;

}
