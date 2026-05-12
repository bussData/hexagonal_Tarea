package com.bancodigital.crud.infraestructure.adapters.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

    private String clienteId;
    private String nombre;
    private String email;
    private String documento;
    private Date fechaCreacion;
}
