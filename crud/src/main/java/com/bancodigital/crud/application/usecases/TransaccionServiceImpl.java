package com.bancodigital.crud.application.usecases;

import com.bancodigital.crud.application.ports.input.TransaccionService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.application.ports.output.CuentaRepositoryPort;
import com.bancodigital.crud.application.ports.output.TransaccionRepositoryPort;
import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.domain.model.Transaccion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepositoryPort transaccionRepositoryPort;
    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    @Override
    public Transaccion realizarTransferencia(Transaccion newTrx) {
        if(newTrx == null || newTrx.getTrxId() == null ){
            throw new RuntimeException("Trx id no puede ser nulo");
        }

        //validaciones de negocio de la trx:
        validacionesRegistroTrx(newTrx);

        Cuenta cuentaOrigen = cuentaRepositoryPort.findById(newTrx.getCuentaOrigen().getCuentaId());
        Cuenta cuentaDestino = cuentaRepositoryPort.findById(newTrx.getCuentaDestino().getCuentaId());

        if(cuentaOrigen == null || cuentaDestino == null){
            throw new RuntimeException("Cuenta de origen o destino debe existir");
        }

        //validaciones de negocio de las ctas:
        validacionesCtasTrx(cuentaOrigen, cuentaDestino);

        //Actualizar saldos:
        Transaccion trxProcesada = actualizarSaldos(newTrx);

        //notificar al cliente:
        cuentaRepositoryPort.notificarCliente(newTrx.getMontoTrx(),
                cuentaOrigen.getCuentaId(), cuentaOrigen.getCliente().getEmail(), cuentaOrigen.getCliente().getNombre(),
                "RETIRO", newTrx.getComision(), newTrx.getCuentaDestino().getCuentaId());
        cuentaRepositoryPort.notificarCliente(newTrx.getMontoTrx(),
                cuentaDestino.getCuentaId(), cuentaDestino.getCliente().getEmail(), cuentaDestino.getCliente().getNombre(),
                "DEPOSITO", new BigDecimal(0), newTrx.getCuentaOrigen().getCuentaId());

        //actualizar estado a notificada:
        return transaccionRepositoryPort.updateTrx(trxProcesada,"NOTIFICADA");
    }

    private void validacionesCtasTrx(Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        //validar que ambas cuentas esten estado ACTIVA

        //Validar saldos de ctas origen y destino
        //cuentaOrigen.getSaldo > 0 (usar compareTo de bigDecimal)

    }

    private Transaccion actualizarSaldos(Transaccion newTrx){
        //Actualizar saldos:
        cuentaRepositoryPort.updateCuenta(newTrx.getMontoTrx(),
                newTrx.getCuentaOrigen().getCuentaId(), "RETIRO", newTrx.getComision());
        cuentaRepositoryPort.updateCuenta(newTrx.getMontoTrx(),
                newTrx.getCuentaDestino().getCuentaId(), "DEPOSITO", new BigDecimal(0));

        return transaccionRepositoryPort.save(newTrx);
    }

    private void validacionesRegistroTrx(Transaccion newTrx) {

        //validar que trxId no exista
        Transaccion lstTrx = transaccionRepositoryPort.findById(newTrx.getTrxId());

        if(lstTrx !=null){
            throw new RuntimeException("Trx id ya existe");
        }

        //validar existencia de ctas y estado activas
        if(newTrx.getCuentaOrigen().getCuentaId() == null || newTrx.getCuentaDestino().getCuentaId() ==null){
            throw new RuntimeException("Cuenta de origen o destino debe ser valida");
        }
        //validar que el tipo de trx sea diferente a null

    }

    @Override
    public Transaccion findTrxById(String trxId) {
        return null;
    }

    @Override
    public List<Transaccion> findAllTransacciones() {
        return transaccionRepositoryPort.findAll();
    }
}
