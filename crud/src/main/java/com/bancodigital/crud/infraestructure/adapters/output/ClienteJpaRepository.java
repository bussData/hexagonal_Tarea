package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface ClienteJpaRepository  extends JpaRepository<ClienteEntity, String> {

    @Query("SELECT u FROM ClienteEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ClienteEntity> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);
    ClienteEntity findByEmail(String email);

}
