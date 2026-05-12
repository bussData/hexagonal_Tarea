package com.bancodigital.crud.application.usecases;

import com.bancodigital.crud.application.ports.input.ClienteService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepositoryPort clienteRepositoryPort;

    @Override
    @Transactional
    public Cliente createCliente(Cliente newCliente) {
        if(newCliente == null){
            throw new RuntimeException("Cliente no puede ser nulo");
        }

        //ejecutar validaciones del cliente.

        return clienteRepositoryPort.save(newCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAllClientes() {
        return clienteRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findClienteById(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new RuntimeException("Id de cliente invalido");
        }
        return clienteRepositoryPort.findClienteById(clienteId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findClienteByName(String nombre) {
        if(nombre ==null || nombre.trim().isEmpty()){
            throw new RuntimeException("Nombre a buscar no puede ser invalido");
        }

        return clienteRepositoryPort.findByNameContaining(nombre.trim());
    }

    @Override
    @Transactional
    public Cliente updateCliente(String id, Cliente cliente) {
        if (id == null || id.trim().isEmpty()) {
            throw new RuntimeException("Id invalido");
        }

        Cliente clienteExistente =  clienteRepositoryPort.findClienteById(id);

        if(clienteExistente == null){
            throw new RuntimeException("Cliente no encontrado");
        }

        //validaciones de cliente
        //seteo de valores nuevos

        return clienteRepositoryPort.save(clienteExistente);
    }

    @Override
    public void deleteCliente(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new RuntimeException("Id de cliente invalido");
        }

        if(clienteRepositoryPort.findClienteById(clienteId)==null){
            throw new RuntimeException("Cliente no existe");
        }

        clienteRepositoryPort.deleteCliente(clienteId);
    }
}
