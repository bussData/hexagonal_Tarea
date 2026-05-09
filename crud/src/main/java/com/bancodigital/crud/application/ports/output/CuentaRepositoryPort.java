package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Cuenta;

import java.util.List;

public interface CuentaRepositoryPort {

    /* estan pidiendo:
     - Crear cuenta (nombre, saldo inicial)
     - Consultar saldo
     */

    Cuenta save(Cuenta cuenta);
    List<Cuenta> findById(String nroCta);
    List<Cuenta> findAll();


}
