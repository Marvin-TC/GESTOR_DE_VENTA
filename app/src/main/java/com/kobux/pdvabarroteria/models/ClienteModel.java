package com.kobux.pdvabarroteria.models;

public class ClienteModel {

    private Long id;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String nit;
    private String correoElectronico;
    private String fechaRegistro;

    public ClienteModel() {
    }
    public ClienteModel( String nombres, String apellidos, String direccion, String nit, String correoElectronico, String fechaRegistro) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.nit = nit;
        this.correoElectronico = correoElectronico;
        this.fechaRegistro = fechaRegistro;
    }
    //getters
    public Long getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNit() {
        return nit;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    //setters
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
