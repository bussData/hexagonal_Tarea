package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Cuenta;

import java.util.List;

public interface CuentaService {

    Cuenta createCuenta(Cuenta newCuenta);
    List<Cuenta> findAllCuentas();
    List<Cuenta> findCuentaById(String cuentaId);

    Cuenta updateCuenta(String cuentaId, Cuenta cuenta);
    void deleteCuenta(String cuentaId);
}
