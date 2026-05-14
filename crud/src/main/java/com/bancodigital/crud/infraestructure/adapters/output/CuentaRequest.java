package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaRequest {

    private String  cuentaId;
    private Cliente cliente;
    private String numCuenta;
    private BigDecimal saldo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;
}
