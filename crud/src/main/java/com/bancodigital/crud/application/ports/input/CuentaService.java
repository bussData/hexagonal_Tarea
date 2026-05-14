package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    Cuenta createCuenta(Cuenta newCuenta);
    List<Cuenta> findAllCuentas();
    //Cuenta findCuentaById(String cuentaId);
    //List<Cuenta> findCuentaByNroCuenta(String nroCuenta);
    List<Cuenta> findCuentaByNombreCliente(String nombre);

    void deleteCuenta(String cuentaId);
}
