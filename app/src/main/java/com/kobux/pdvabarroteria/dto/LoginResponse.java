package com.kobux.pdvabarroteria.dto;

public class LoginResponse {

    private long id;
    private String nombre;
    private String email;

    public LoginResponse(long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}