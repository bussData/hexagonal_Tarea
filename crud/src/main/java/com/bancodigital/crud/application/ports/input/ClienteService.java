package com.bancodigital.crud.application.ports.input;

import com.bancodigital.crud.domain.model.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente createCliente(Cliente newCliente);
    List<Cliente> findAllClientes();
   //  Cliente findById(String clienteId);
   Cliente findCliente(String clienteId);

    List<Cliente> findClienteByName(String nombre);

    Cliente updateCliente(String id, Cliente cliente);
    void deleteCliente(String clienteId);
}
