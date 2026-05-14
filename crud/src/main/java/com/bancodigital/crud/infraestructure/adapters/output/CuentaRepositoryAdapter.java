package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.application.ports.output.CuentaRepositoryPort;
import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.infraestructure.adapters.output.entities.CuentaEntity;
import com.bancodigital.crud.infraestructure.adapters.output.CuentaJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//IMPLEMENTACION DE ADAPTER QUE SE LEE EN EL CRUD CONFIG
@Repository
@RequiredArgsConstructor
@Slf4j
public class CuentaRepositoryAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository jpaRepository;
    private final CuentaMapper mapper;

    @Override
    public Cuenta save(Cuenta cuenta) {
        log.info("INICIO SAVE");
        CuentaEntity entity = this.mapper.toEntity(cuenta);
        log.info("ENTITY: {}", entity);

        CuentaEntity entidadAGrabar = this.jpaRepository.save(entity);
        log.info("GUARDADO: {}", entidadAGrabar);
        Cuenta savedCuenta = this.mapper.toDomain(entidadAGrabar);
        log.info("se grabo cuenta");
        return savedCuenta;
    }

    @Override
    public List<Cuenta> findByNroCuenta(String nroCta) {
        List<CuentaEntity> entities = this.jpaRepository.findByNroCuenta(nroCta);
        return this.mapper.toDomain(entities);
    }

    @Override
    public List<Cuenta> findByClienteId(String clienteId) {
        List<CuentaEntity> entities = this.jpaRepository.findByClienteId(clienteId);
        return this.mapper.toDomain(entities);
    }

    @Override
    public Cuenta findById(String cuentaId) {

        /*CuentaEntity entity = this.jpaRepository.findById(cuentaId)
                .orElseThrow(() ->
                        new RuntimeException("No hay cuentas"));
        return this.mapper.toDomain(entity);*/
        Optional<Cuenta> cuenta = this.jpaRepository.findById(cuentaId)
                .map(this.mapper::toDomain);

        if(cuenta.isEmpty()){
            log.info("cuenta no encontrada con id: ", cuentaId);
        }
        return cuenta.orElse(null);
    }

    @Override
    public List<Cuenta> findAll() {
        List<CuentaEntity> entities = this.jpaRepository.findAll();
        return this.mapper.toDomain(entities);
    }

    @Override
    public boolean esCuentaUnica(Cuenta cuenta) {
        //Cotejar si es bd es la unica cta
        if (this.jpaRepository.esCuentaUnica(cuenta.getCuentaId())) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteCuenta(String nroCta) {
        this.jpaRepository.deleteById(nroCta);
    }

    @Override
    public Cuenta updateCuenta(BigDecimal monto, String cuentaId, String operacion,
                               BigDecimal comision) {
        if(new BigDecimal(0).compareTo(monto)==0){
            throw new RuntimeException("monto invalido");
        }

        Cuenta cuentaExistente = this.findById(cuentaId);

        if(cuentaExistente == null){
            throw new RuntimeException("Cuenta no encontrada");
        }

        if(new BigDecimal(0).compareTo(cuentaExistente.getSaldo())==0){
            throw new RuntimeException("No hay saldo");
        }
        if(operacion.equals("RETIRO") && monto.compareTo(cuentaExistente.getSaldo())>0){
            throw new RuntimeException("Monto solicitado "+monto+" excede al saldo actual "+cuentaExistente.getSaldo());
        }

        if(operacion.equals("RETIRO") && comision.compareTo(new BigDecimal(0))>0){
            monto = monto.add(comision);
        }
        BigDecimal nuevoSaldo = operacion.equals("DEPOSITO")?
                cuentaExistente.getSaldo().add(monto):cuentaExistente.getSaldo().subtract(monto);
        cuentaExistente.updateCuenta(nuevoSaldo);
        return this.save(cuentaExistente);
    }

    @Override
    public void notificarCliente(BigDecimal monto, String cuentaId, String email, String nombre, String operacion,
                                 BigDecimal comision, String cuentaFondo) {

        log.info("******* NOTIFICACION ***************");
        log.info("TO: "+email);
        log.info("ASUNTO: TRANSFERENCIA");
        log.info("**");
        log.info("Sr. "+nombre+",");
        log.info("Se ha realizado exitosamente la transferencia de: ");
        if(operacion.equals("RETIRO")){
            log.info("Monto: "+monto);
            log.info("Comision: " + comision);
            log.info(" de su cta "+cuentaId+" a la cuenta "+cuentaFondo);
        }else{
            log.info("Monto: "+monto);
            log.info(" de la cta "+cuentaFondo+" a su cuenta "+cuentaId);
        }
        log.info("******* FIN NOTIFICACION ***************");

    }
}