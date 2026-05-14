package com.bancodigital.crud.infraestructure.adapters.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponse {

    private String  cuentaId;
    private String numCuenta;
    private BigDecimal saldo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;
}
