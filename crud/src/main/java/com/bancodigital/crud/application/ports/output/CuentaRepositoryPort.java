package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Cuenta;

import java.math.BigDecimal;
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

    boolean esCuentaUnica(Cuenta cuenta);
    void deleteCuenta(String nroCta);
    Cuenta updateCuenta(BigDecimal monto, String cuentaId, String operacion,
                        BigDecimal comision);

    void notificarCliente(BigDecimal monto, String cuentaId, String email, String nombre, String operacion,
                        BigDecimal comision, String cuentaFondo);

}
