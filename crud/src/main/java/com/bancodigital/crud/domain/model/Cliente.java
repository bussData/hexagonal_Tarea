package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Cliente {
    private Long clienteId;
    private String nombre;
    private String email;
    private String tipoDocumento;
    private String numDocumento;
    private Date fechaCreacion;


    public boolean hasValidEmail() {
        return email != null &&
                email.contains("@") &&
                email.contains(".") &&
                email.length() > 5;
    }

    public boolean hasValidNombre() {
        return nombre != null &&
                !nombre.trim().isEmpty() &&
                nombre.length() >= 2;
    }


    public boolean hasValidDocumento() {
        return tipoDocumento != null &&
                !tipoDocumento.trim().isEmpty() &&
                !numDocumento.trim().isEmpty() &&
                 numDocumento.length() >= 8;
    }


}
