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
    public Cuenta findById(String cuentaId) {

        CuentaEntity entity = this.jpaRepository.findById(cuentaId)
                 .orElseThrow(() ->
                    new RuntimeException("No hay resultados"));
        return this.mapper.toDomain(entity);

    }

    @Override
    public List<Cuenta> findAll() {
        List<CuentaEntity> entities = this.jpaRepository.findAll();
        return this.mapper.toDomain(entities);
    }

    @Override
    public boolean tieneSaldo(Cuenta cuenta) {
        if(this.jpaRepository.tieneSaldo(cuenta.getCuentaId())){
            return true;
        }
        return false;
    }

    @Override
    public boolean esCuentaUnica(Cuenta cuenta) {
        //Cotejar si es bd es la unica cta
        if(this.jpaRepository.esCuentaUnica(cuenta.getCliente().getClienteId())){
            return true;
        }
        return false;
    }

    @Override
    public boolean esCuentaActiva(Cuenta cuenta) {
        //Cotejar si en bd la cuenta esta activa
        return false;
    }

    @Override
    public void deleteCuenta(String nroCta) {
        this.jpaRepository.deleteById(nroCta);
    }
}
