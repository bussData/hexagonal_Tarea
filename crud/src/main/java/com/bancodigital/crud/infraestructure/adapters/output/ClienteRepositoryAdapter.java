package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//IMPLEMENTACION DE ADAPTER QUE SE LEE EN EL CRUD CONFIG
@Repository
@RequiredArgsConstructor
@Slf4j
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final Map<Long, Cliente> data = new HashMap<>();
    private final ClienteJpaRepository jpaRepository;
    private final ClienteMapper mapper;
    @Override
    public Cliente save(Cliente cliente) {

        log.info("INICIO SAVE");
        ClienteEntity entity = this.mapper.toEntity(cliente);
        log.info("ENTITY: {}", entity);

        ClienteEntity entidadAGrabar = this.jpaRepository.save(entity);
        log.info("GUARDADO: {}", entidadAGrabar);
        Cliente savedCliente = this.mapper.toDomain(entidadAGrabar);
        log.info("se grabo cliente");
        return savedCliente;
    }

    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Cliente findClienteById(String clienteId) {
        return data.get(clienteId);
    }

    @Override
    public List<Cliente> findByNameContaining(String name) {
        return data.values()
                .stream()
                .filter(c -> c.getNombre().contains(name))
                .toList();
    }

    @Override
    public void deleteCliente(String clienteId) {
        data.remove(clienteId);
    }
}
