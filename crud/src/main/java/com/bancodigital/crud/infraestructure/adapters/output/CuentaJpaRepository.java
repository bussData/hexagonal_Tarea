package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.infraestructure.adapters.output.entities.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface CuentaJpaRepository  extends JpaRepository<CuentaEntity, String> {

    @Query("SELECT u FROM CuentaEntity u WHERE u.numCuenta = :nroCta")
    List<CuentaEntity> findByNroCuenta(@Param("nroCta") String nroCta);

    @Query("SELECT CASE WHEN u.saldo>0 THEN true ELSE false END FROM CuentaEntity u WHERE u.cuentaId = :cuentaId")
    boolean tieneSaldo(@Param("cuentaId") String cuentaId);

    @Query("SELECT CASE WHEN COUNT(1)>=1 THEN false ELSE true END FROM CuentaEntity u WHERE u.cliente.id =:clienteId")
    boolean esCuentaUnica(@Param("clienteId") String clienteId);
    //boolean esCuentaActiva(Cuenta cuenta);

}
