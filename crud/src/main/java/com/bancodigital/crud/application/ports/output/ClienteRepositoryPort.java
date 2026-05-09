package com.bancodigital.crud.application.ports.output;

import com.bancodigital.crud.domain.model.Cliente;

import java.util.List;

public interface ClienteRepositoryPort {

    /* estan pidiendo:
     - Crear cuenta (nombre, saldo inicial)
     - Consultar saldo
     */

    Cliente save(Cliente cliente);
    List<Cliente> findAll();
    List<Cliente> findByNameContaining(String name);
}
