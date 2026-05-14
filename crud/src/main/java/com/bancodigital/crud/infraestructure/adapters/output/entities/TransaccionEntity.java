package com.bancodigital.crud.infraestructure.adapters.output.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionEntity {
    @Id
    @Column(name = "transaccion_id" , nullable = false, length = 50)
    private String transaccionId;

    @ManyToOne(fetch = FetchType.LAZY) //con esto mapeo el FK
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    private CuentaEntity cuentaOrigenId;

    @ManyToOne(fetch = FetchType.LAZY) //con esto mapeo el FK
    @JoinColumn(name = "cuenta_destino_id", nullable = false)
    private CuentaEntity cuentaDestinoId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal comision;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion  = LocalDateTime.now();

}
