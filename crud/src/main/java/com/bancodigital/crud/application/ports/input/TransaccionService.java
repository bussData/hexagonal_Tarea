package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Transaccion;

import java.util.List;

public interface TransaccionService {

    Transaccion createTrx(Transaccion newTrx);
    Transaccion findTrxById(String trxId);
    List<Transaccion> findAllTransacciones();

    Transaccion updateTrx(String trxId, Transaccion trx);
    void deleteTransaccion(String trxId);

}
