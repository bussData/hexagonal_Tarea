package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import com.bancodigital.crud.infraestructure.adapters.output.entities.ClienteEntity;
import com.bancodigital.crud.infraestructure.adapters.output.ClienteJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.UUID;

//IMPLEMENTACION DE ADAPTER QUE SE LEE EN EL CRUD CONFIG
@Repository
@RequiredArgsConstructor
@Slf4j
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;
    private final ClienteMapper mapper;
    @Override
    public Cliente save(Cliente cliente) {

        log.info("INICIO SAVE");
        ClienteEntity entity = this.mapper.toEntity(cliente);

        // Generar UUID si el cliente no trae ID (nuevo registro)
        if (entity.getId() == null || entity.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        }

        log.info("ENTITY: {}", entity!=null?entity:null);

        ClienteEntity entidadAGrabar = this.jpaRepository.save(entity);
        log.info("GUARDADO: {}", entidadAGrabar);
        Cliente savedCliente = this.mapper.toDomain(entidadAGrabar);
        log.info("se grabo cliente");
        return savedCliente;
    }

    @Override
    public List<Cliente> findAll() {
        List<ClienteEntity> entities = this.jpaRepository.findAll();
        return this.mapper.toDomain(entities);
    }

    @Override
    public Cliente findById(String clienteId) {

        /*return this.jpaRepository
                .findById(clienteId)
                .map(this.mapper::toDomain)
                .orElse(null);//en caso de no encontrar devuelve null*/
        Optional<Cliente> cliente = this.jpaRepository
                .findById(clienteId)
                .map(this.mapper::toDomain);

        if (cliente.isEmpty()) {
            log.info("Cliente no encontrado con id: {}", clienteId);
        }

        return cliente.orElse(null);
    }

    @Override
    public List<Cliente> findByNameContaining(String name) {

        List<ClienteEntity> entities = this.jpaRepository.findByNameContainingIgnoreCase(name);
        return this.mapper.toDomain(entities);
    }

    @Override
    public void deleteCliente(String clienteId) {
        this.jpaRepository.deleteById(clienteId);
    }

    @Override
    public boolean existeByEmail(String email) {
        return this.jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existeByDocumento(String documento) {
        return this.jpaRepository.existsByDocumento(documento);
    }
}
