package com.kobux.pdvabarroteria.models;

public class CompraModel {

    private Long id;
    private Long proveedorId;
    private String fechaCompra;
    private double total;
    private double descuentoAplicado;
    private String numeroFactura;
    private String serieFactura;

    public CompraModel() {
    }

    public CompraModel(long id, long proveedorId, String fechaCompra, double total, double descuentoAplicado, String numeroFactura, String serieFactura) {
        this.id = id;
        this.proveedorId = proveedorId;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.descuentoAplicado = descuentoAplicado;
        this.numeroFactura = numeroFactura;
        this.serieFactura = serieFactura;
    }

    //getters
    public Long getId() {
        return id;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public double getTotal() {
        return total;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    //setters
    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }
}
