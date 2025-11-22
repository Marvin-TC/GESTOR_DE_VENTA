package com.kobux.pdvabarroteria.models;

public class DetalleCompraModel {
    private Long id;
    private Long comprasId;
    private Long productosId;
    private int cantidad;
    private double costoUnitario;

    public DetalleCompraModel() {
    }
    public DetalleCompraModel(long id, long comprasId, long productosId, int cantidad, double costoUnitario) {
        this.id = id;
        this.comprasId = comprasId;
        this.productosId = productosId;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
    }
    //getters
    public Long getId() {
        return id;
    }

    public Long getComprasId() {
        return comprasId;
    }

    public Long getProductosId() {
        return productosId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }
    //setters
    public void setComprasId(Long comprasId) {
        this.comprasId = comprasId;
    }

    public void setProductosId(Long productosId) {
        this.productosId = productosId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
}
