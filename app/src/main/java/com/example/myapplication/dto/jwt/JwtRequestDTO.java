package com.example.myapplication.dto.jwt;

import java.io.Serializable;

public class JwtRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String identificador;
    private String password;

    public JwtRequestDTO() {
    }

    public JwtRequestDTO(String identificador, String password) {
        this.setIdentificador(identificador);
        this.setPassword(password);
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
