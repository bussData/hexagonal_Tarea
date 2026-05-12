package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Transaccion;

import java.util.List;

public interface TransaccionService {

    Transaccion createTrx(Transaccion newTrx);
    Transaccion findTrxById(Long trxId);
    List<Transaccion> findAllTransacciones();

    Transaccion updateTrx(Long trxId, Transaccion trx);
    void deleteTransaccion(Long trxId);

}
