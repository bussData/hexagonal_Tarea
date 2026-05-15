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
        // Validar que ambas cuentas estén en estado ACTIVO
        if (!"ACTIVO".equals(cuentaOrigen.getEstado())) {
            throw new RuntimeException("La cuenta origen no está activa. Estado actual: " + cuentaOrigen.getEstado());
        }
        if (!"ACTIVO".equals(cuentaDestino.getEstado())) {
            throw new RuntimeException("La cuenta destino no está activa. Estado actual: " + cuentaDestino.getEstado());
        }

        // Validar que la cuenta origen tenga saldo suficiente (saldo > 0 y >= monto + comision)
        if (cuentaOrigen.getSaldo() == null || cuentaOrigen.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("La cuenta origen no tiene saldo disponible");
        }
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

        // Validar que trxId no exista ya en BD
        Transaccion lstTrx = transaccionRepositoryPort.findById(newTrx.getTrxId());
        if (lstTrx != null) {
            throw new RuntimeException("Trx id ya existe");
        }

        // Validar existencia de IDs de cuentas origen y destino
        if (newTrx.getCuentaOrigen() == null || newTrx.getCuentaOrigen().getCuentaId() == null) {
            throw new RuntimeException("La cuenta origen es obligatoria");
        }
        if (newTrx.getCuentaDestino() == null || newTrx.getCuentaDestino().getCuentaId() == null) {
            throw new RuntimeException("La cuenta destino es obligatoria");
        }

        // Validar que origen ≠ destino (database.md: cuenta_origen_id != cuenta_destino_id)
        if (!newTrx.cuentasDistintas()) {
            throw new RuntimeException("La cuenta origen y destino no pueden ser la misma");
        }

        // Validar monto > 0 (database.md: monto > 0)
        if (!newTrx.hasValidMonto()) {
            throw new RuntimeException("El monto debe ser mayor a 0. Monto recibido: " + newTrx.getMontoTrx());
        }

        // Validar tipo de trx (database.md: 'TRANSFERENCIA', 'DEPOSITO', 'RETIRO')
        if (!newTrx.hasValidTipo()) {
            throw new RuntimeException("El tipo de transacción '" + newTrx.getTipoTrx() + "' no es válido. Use: TRANSFERENCIA, DEPOSITO o RETIRO");
        }

        // Validar estado de trx (database.md: 'PENDIENTE', 'COMPLETADA', 'FALLIDA', 'CANCELADA')
        if (!newTrx.hasValidEstado()) {
            throw new RuntimeException("El estado de transacción '" + newTrx.getEstadoTrx() + "' no es válido. Use: PENDIENTE, COMPLETADA, FALLIDA o CANCELADA");
        }
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
