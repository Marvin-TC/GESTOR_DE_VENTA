package com.kobux.pdvabarroteria.models;

public class ProveedorModel {
    private Long id;
    private String nombreEmpresa;
    private String direccion;
    private String telefono;
    private String nombreVendedor;
    private String telefonoVendedor;
    private String fechaRegistro;

    public ProveedorModel() {
    }
    public ProveedorModel(String nombreEmpresa, String direccion, String telefono, String nombreVendedor, String telefonoVendedor, String fechaRegistro) {
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreVendedor = nombreVendedor;
        this.telefonoVendedor = telefonoVendedor;
        this.fechaRegistro = fechaRegistro;
    }
    //getters
    public Long getId() {
        return id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public String getTelefonoVendedor() {
        return telefonoVendedor;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    //setters
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public void setTelefonoVendedor(String telefonoVendedor) {
        this.telefonoVendedor = telefonoVendedor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
