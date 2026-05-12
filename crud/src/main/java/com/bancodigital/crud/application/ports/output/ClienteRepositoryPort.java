package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    /* estan pidiendo:
     - Crear cuenta (nombre, saldo inicial)
     - Consultar saldo
     */

    Cliente save(Cliente cliente);
    List<Cliente> findAll();
    Cliente findById(String clienteId);
    List<Cliente> findByNameContaining(String name);
    boolean existeByEmail(String email);
    boolean existeByDocumento(String documento);
    void deleteCliente(String clienteId);
}
