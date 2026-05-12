package com.bancodigital.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class Cliente {
    private String clienteId;
    private String nombre;
    private String email;
    private String documento;
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
        return documento != null &&
                !documento.trim().isEmpty() &&
                documento.length() >= 8;
    }

    public void updateCliente(String nombre, String email, String documento){
        this.nombre = nombre;
        this.email = email;
        this.documento = documento;
    }

}
