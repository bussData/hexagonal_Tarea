package com.bancodigital.crud.infraestructure.adapters.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {

    private String trxId;
    private String cuentaOrigenId;
    private String cuentaDestinoId;
    private BigDecimal montoTrx;
    private BigDecimal comision;
    private String tipoTrx;
    private String estadoTrx;
    private String descripcionTrx;
    private Date fechaTrx;
}
