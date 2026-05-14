package com.bancodigital.crud.infraestructure.adapters.output;

import com.bancodigital.crud.domain.model.Cliente;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    private String cuentaId;

    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 20, message = "El número de cuenta no puede superar los 20 caracteres")
    private String numCuenta;

    @NotNull(message = "El saldo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|CERRADO", message = "El estado debe ser 'ACTIVO' o 'CERRADO'")
    private String estado;

    private Date fechaCreacion;
    private Date fechaActualizacion;
}
