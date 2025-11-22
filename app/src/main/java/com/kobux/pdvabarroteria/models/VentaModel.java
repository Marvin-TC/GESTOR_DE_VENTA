package com.kobux.pdvabarroteria.models;

public class VentaModel {
    private Long id;
    private Long clienteId;
    private String fechaVenta;
    private String horaVenta;
    private String vendedor;
    private double total;
    private double descuento;

    public VentaModel() {
    }
    public VentaModel(long id, long clienteId, String fechaVenta, String horaVenta, String vendedor, double total, double descuento) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.vendedor = vendedor;
        this.total = total;
        this.descuento = descuento;
    }
    //getters
    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public String getHoraVenta() {
        return horaVenta;
    }

    public String getVendedor() {
        return vendedor;
    }

    public double getTotal() {
        return total;
    }

    public double getDescuento() {
        return descuento;
    }
    //setters
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setHoraVenta(String horaVenta) {
        this.horaVenta = horaVenta;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
