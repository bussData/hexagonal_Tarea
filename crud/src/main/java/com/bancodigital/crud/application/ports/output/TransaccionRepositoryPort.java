package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Transaccion;

import java.util.List;

public interface TransaccionRepositoryPort {

     /* estan pidiendo:
     - Transferir dinero entre cuentas
     - Validar saldo suficiente
     -  Notificar al cliente después de transferencia
     -  Soportar 1 canal (consola)
     */

    Transaccion save(Transaccion trx);
    List<Transaccion> findAll();
    Transaccion findById(String numTrx);
    Transaccion updateTrx(Transaccion trx, String estado);
}
