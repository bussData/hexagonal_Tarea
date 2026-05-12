package com.bancodigital.crud.infraestructure.adapters.output;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "cliente")
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {
    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String documento;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion  = LocalDateTime.now();

}
