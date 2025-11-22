package com.kobux.pdvabarroteria.models;

public class ProductoModel {
    private Long id;
    private String nombre;
    private String nombreFacturacion;
    private String codigoInterno;
    private String codigoBarra;
    private String urlImagen;
    private double precioUnidad;
    private double precioMayorista;
    private int stock;
    private boolean estado;
    private String fechaRegistro;

    public ProductoModel() {
    }

    public ProductoModel(long id, String nombre, String nombreFacturacion, String codigoInterno, String codigoBarra, String urlImagen, double precioUnidad, double precioMayorista, int stock, boolean estado, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.nombreFacturacion = nombreFacturacion;
        this.codigoInterno = codigoInterno;
        this.codigoBarra = codigoBarra;
        this.urlImagen = urlImagen;
        this.precioUnidad = precioUnidad;
        this.precioMayorista = precioMayorista;
        this.stock = stock;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    //getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreFacturacion() {
        return nombreFacturacion;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public double getPrecioMayorista() {
        return precioMayorista;
    }

    public int getStock() {
        return stock;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    //setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombreFacturacion(String nombreFacturacion) {
        this.nombreFacturacion = nombreFacturacion;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public void setPrecioMayorista(double precioMayorista) {
        this.precioMayorista = precioMayorista;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}


