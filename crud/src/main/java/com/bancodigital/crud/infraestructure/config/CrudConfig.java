package com.bancodigital.crud.infraestructure.config;

import com.bancodigital.crud.application.ports.input.ClienteService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.application.usecases.ClienteServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrudConfig {

    @Bean
    public ClienteService clienteService(ClienteRepositoryPort clienteRepositoryPort){
        return new ClienteServiceImpl(clienteRepositoryPort);
    }
}
