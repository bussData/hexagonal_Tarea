package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Transaccion;
import com.bancodigital.crud.infraestructure.adapters.output.entities.TransaccionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = CuentaMapper.class)
public interface TransaccionMapper {

    // Request to Domain (for new trx)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "trxId", source = "trxId")
    @Mapping(target = "cuentaOrigen.cuentaId", source = "cuentaOrigenId")
    @Mapping(target = "cuentaDestino.cuentaId", source = "cuentaDestinoId")
    @Mapping(target = "montoTrx", source = "montoTrx")
    @Mapping(target = "comision", source = "comision")
    @Mapping(target = "tipoTrx", source = "tipoTrx")
    @Mapping(target = "estadoTrx", source = "estadoTrx")
    @Mapping(target = "descripcionTrx", source = "descripcionTrx")
    Transaccion toDomain(TransaccionRequest request);

    //DOmain to Entity
    @Mapping(target = "transaccionId", source = "trxId")
    @Mapping(target = "cuentaOrigenId", source = "cuentaOrigen")
    @Mapping(target = "cuentaDestinoId", source = "cuentaDestino")
    @Mapping(target = "monto", source = "montoTrx")
    @Mapping(target = "comision", source = "comision")
    @Mapping(target = "tipo", source = "tipoTrx")
    @Mapping(target = "estado", source = "estadoTrx")
    @Mapping(target = "descripcion", source = "descripcionTrx")
    @Mapping(target = "fechaCreacion", ignore = true)
    TransaccionEntity toEntity(Transaccion trx);

    // Entity -> Domain
    @Mapping(target = "trxId", source = "transaccionId")
    @Mapping(target = "cuentaOrigen", source = "cuentaOrigenId")
    @Mapping(target = "cuentaDestino", source = "cuentaDestinoId")
    @Mapping(target = "montoTrx", source = "monto")
    @Mapping(target = "comision", source = "comision")
    @Mapping(target = "tipoTrx", source = "tipo")
    @Mapping(target = "estadoTrx", source = "estado")
    @Mapping(target = "descripcionTrx", source = "descripcion")
    @Mapping(target = "fechaTrx", source = "fechaCreacion")
    Transaccion toDomain(TransaccionEntity entity);


    //Domain to Response
    @Mapping(target = "trxId", source = "trxId")
    @Mapping(target = "cuentaOrigenId", source = "cuentaOrigen.cuentaId")
    @Mapping(target = "cuentaDestinoId", source = "cuentaDestino.cuentaId")
    @Mapping(target = "montoTrx", source = "montoTrx")
    @Mapping(target = "comision", source = "comision")
    @Mapping(target = "tipoTrx", source = "tipoTrx")
    @Mapping(target = "estadoTrx", source = "estadoTrx")
    @Mapping(target = "descripcionTrx", source = "descripcionTrx")
    TransaccionResponse toResponse(Transaccion createTrx);


    List<TransaccionResponse> toResponse(List<Transaccion> createTrx);
    List<Transaccion> toDomain(List<TransaccionEntity> entities);

}
