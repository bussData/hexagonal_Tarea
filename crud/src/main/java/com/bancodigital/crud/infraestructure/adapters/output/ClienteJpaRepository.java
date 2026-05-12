package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

interface ClienteJpaRepository  extends JpaRepository<ClienteEntity, String> {


}
