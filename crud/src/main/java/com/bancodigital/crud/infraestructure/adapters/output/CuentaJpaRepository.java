package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.infraestructure.adapters.output.entities.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface CuentaJpaRepository  extends JpaRepository<CuentaEntity, String> {

    @Query("SELECT u FROM CuentaEntity u WHERE u.numCuenta = :nroCta")
    List<CuentaEntity> findByNroCuenta(@Param("nroCta") String nroCta);

    @Query("SELECT u FROM CuentaEntity  u WHERE u.cliente.id = :clienteId")
    List<CuentaEntity> findByClienteId(@Param("clienteId") String clienteId);

    @Query("SELECT CASE WHEN u.saldo>0 THEN true ELSE false END FROM CuentaEntity u WHERE u.cuentaId = :cuentaId")
    boolean tieneSaldo(@Param("cuentaId") String cuentaId);

    @Query("SELECT CASE WHEN COUNT(1)=0 THEN true ELSE false END FROM CuentaEntity u WHERE u.cuentaId =:cuentaId")
    boolean esCuentaUnica(@Param("cuentaId") String cuentaId);
    //boolean esCuentaActiva(Cuenta cuenta);

}
