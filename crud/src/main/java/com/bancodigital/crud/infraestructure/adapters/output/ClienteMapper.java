package com.bancodigital.crud.infraestructure.adapters.output;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.bancodigital.crud.domain.model.Cliente;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // Request to Domain (for new users)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "clienteId", source = "clienteId")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "documento", source = "documento")
    Cliente toDomain(ClienteRequest request);


    // Domain to Response
    @Mapping(target = "clienteId", source = "clienteId")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "documento", source = "documento")
    ClienteResponse toResponse(Cliente createCliente);


    List<ClienteResponse> toResponse(List<Cliente> createCliente);


    // Domain to Entity
    @Mapping(target = "id", source = "clienteId")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "documento", source = "documento")
    @Mapping(target = "fechaCreacion", ignore = true)
    ClienteEntity toEntity(Cliente cliente);


    // Entity -> Domain

    Cliente toDomain(ClienteEntity entity);
}
