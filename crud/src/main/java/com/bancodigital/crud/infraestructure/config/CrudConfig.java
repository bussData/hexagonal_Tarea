package com.bancodigital.crud.infraestructure.config;

import com.bancodigital.crud.application.ports.input.ClienteService;
import com.bancodigital.crud.application.ports.input.CuentaService;
import com.bancodigital.crud.application.ports.input.TransaccionService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.application.ports.output.CuentaRepositoryPort;
import com.bancodigital.crud.application.ports.output.TransaccionRepositoryPort;
import com.bancodigital.crud.application.usecases.ClienteServiceImpl;
import com.bancodigital.crud.application.usecases.CuentaServiceImpl;
import com.bancodigital.crud.application.usecases.TransaccionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrudConfig {

    @Bean
    public ClienteService clienteService(ClienteRepositoryPort clienteRepositoryPort){
        return new ClienteServiceImpl(clienteRepositoryPort);
    }

    @Bean
    public CuentaService cuentaService(CuentaRepositoryPort cuentaRepositoryPort,
                                       ClienteRepositoryPort clienteRepositoryPort){
        return new CuentaServiceImpl(cuentaRepositoryPort, clienteRepositoryPort);
    }

    @Bean
    public TransaccionService transaccionService(TransaccionRepositoryPort transaccionRepositoryPort,
                                                 CuentaRepositoryPort cuentaRepositoryPort,
                                                 ClienteRepositoryPort clienteRepositoryPort){
        return new TransaccionServiceImpl(transaccionRepositoryPort,cuentaRepositoryPort, clienteRepositoryPort);
    }
}
