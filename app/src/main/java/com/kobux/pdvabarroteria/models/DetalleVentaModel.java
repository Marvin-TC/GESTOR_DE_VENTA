package com.kobux.pdvabarroteria.models;

public class DetalleVentaModel {
    private Long id;
    private Long ventasId;
    private Long productoId;
    private int cantidad;
    private double precio;
    private double descuento;
    private double subtotal;

    public DetalleVentaModel() {
    }

    public DetalleVentaModel(long id, long ventasId, long productoId, int cantidad, double precio, double descuento, double subtotal) {
        this.id = id;
        this.ventasId = ventasId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.subtotal = subtotal;
    }
    //getters
    public Long getId() {
        return id;
    }

    public Long getVentasId() {
        return ventasId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    //setters
    public void setVentasId(Long ventasId) {
        this.ventasId = ventasId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
