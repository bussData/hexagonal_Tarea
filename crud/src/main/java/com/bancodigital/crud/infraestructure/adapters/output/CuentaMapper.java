package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.infraestructure.adapters.output.entities.CuentaEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

//Se indica que el mapper tbm se alimenta del clienteMapper
@Mapper(componentModel = "spring",
        uses = ClienteMapper.class)
public interface CuentaMapper {

    // Request to Domain (for new cuenta)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cuentaId", source = "cuentaId")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "numCuenta", source = "numCuenta")
    @Mapping(target = "saldo", source = "saldo")
    @Mapping(target = "estado", source = "estado")
    Cuenta toDomain(CuentaRequest request);

    //Domain to Response
    @Mapping(target = "cuentaId", source = "cuentaId")
    @Mapping(target = "numCuenta", source = "numCuenta")
    @Mapping(target = "saldo", source = "saldo")
    @Mapping(target = "estado", source = "estado")
    CuentaResponse toResponse(Cuenta createCuenta);

    @Mapping(target = "cuentaId", source = "cuentaId")
    List<CuentaResponse> toResponse(List<Cuenta> createCuenta);

    //DOmain to Entity
    @Mapping(target = "cuentaId", source = "cuentaId")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "numCuenta", source = "numCuenta")
    @Mapping(target = "saldo", source = "saldo")
    @Mapping(target = "estado", source = "estado")
    CuentaEntity toEntity(Cuenta cuenta);

    // Entity -> Domain
    @Mapping(target = "cuentaId", source = "cuentaId")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "numCuenta", source = "numCuenta")
    Cuenta toDomain(CuentaEntity entity);

    List<Cuenta> toDomain(List<CuentaEntity> entities);

}
