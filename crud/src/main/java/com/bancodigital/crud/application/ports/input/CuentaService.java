package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Cuenta;

import java.util.List;

public interface CuentaService {

    Cuenta createCuenta(Cuenta newCuenta);
    List<Cuenta> findAllCuentas();
    List<Cuenta> findCuentaById(Long cuentaId);

    Cuenta updateCuenta(Long cuentaId, Cuenta cuenta);
    void deleteCuenta(Long cuentaId);
}
