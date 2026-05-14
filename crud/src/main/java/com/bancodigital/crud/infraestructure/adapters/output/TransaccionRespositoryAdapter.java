package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.application.ports.output.TransaccionRepositoryPort;
import com.bancodigital.crud.domain.model.Transaccion;
import com.bancodigital.crud.infraestructure.adapters.output.entities.TransaccionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransaccionRespositoryAdapter implements TransaccionRepositoryPort {

    private final TransaccionJpaRepository jpaRepository;
    private final TransaccionMapper mapper;

    @Override
    public Transaccion save(Transaccion trx) {
        log.info("INICIO SAVE");
        TransaccionEntity entity = this.mapper.toEntity(trx);
        log.info("ENTITY: {}", entity);

        TransaccionEntity entidadAGrabar = this.jpaRepository.save(entity);
        log.info("GUARDADO: {}", entidadAGrabar);
        Transaccion savedTrx = this.mapper.toDomain(entidadAGrabar);
        log.info("se grabo trx");
        return savedTrx;
    }

    @Override
    public List<Transaccion> findAll() {
        List<TransaccionEntity> entities = this.jpaRepository.findAll();
        return this.mapper.toDomain(entities);
    }

    @Override
    public Transaccion findById(String numTrx) {
        Optional<Transaccion> trx = this.jpaRepository.findById(numTrx)
                .map(this.mapper::toDomain);
        if(trx.isEmpty()){
            log.info("trx no encontrada con id: ", numTrx);
        }

        return trx.orElse(null);
    }

    @Override
    public Transaccion updateTrx(Transaccion trx, String estado) {
        if( trx == null){
            throw new RuntimeException("Trx invalido");
        }
        trx.updateEstado(estado);
        return this.save(trx);
    }
}
