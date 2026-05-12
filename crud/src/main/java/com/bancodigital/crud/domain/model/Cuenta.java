package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Cuenta {
    private String cuentaId;
    private String clienteId;
    private String numCuenta;
    private double saldo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;

}
