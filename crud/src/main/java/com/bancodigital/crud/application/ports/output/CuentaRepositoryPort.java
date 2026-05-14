package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Cuenta;

import java.util.List;

public interface CuentaRepositoryPort {

    /* estan pidiendo:
     - Crear cuenta (nombre, saldo inicial)
     - Consultar saldo
     */

    Cuenta save(Cuenta cuenta);
    Cuenta findById(String cuentaId);
    List<Cuenta> findByNroCuenta(String nroCta);
    List<Cuenta> findByClienteId(String clienteId);
    List<Cuenta> findAll();

    boolean tieneSaldo(Cuenta cuenta);
    boolean esCuentaUnica(Cuenta cuenta);
    boolean esCuentaActiva(Cuenta cuenta);

    void deleteCuenta(String nroCta);

}
