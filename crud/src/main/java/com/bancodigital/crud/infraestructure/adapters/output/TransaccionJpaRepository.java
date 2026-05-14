package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.infraestructure.adapters.output.entities.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransaccionJpaRepository  extends JpaRepository<TransaccionEntity, String> {

}
