package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Cuenta {
    private Long cuentaId;
    private Long clienteId;
    private String numCuenta;
    private double saldo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;

}
