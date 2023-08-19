package com.example.dnisearch.model;

import java.util.Date;

public class Movimiento {
    private int id;
    private String detalle;
    private float total;
    private String tipoMovimiento;
    private Date fecha_create;
    private Date fecha_update;
    private String tarjeta_id;

    public Movimiento(String detalle, float total, String tipoMovimiento, String tarjetaId) {
        this.detalle = detalle;
        this.total = total;
        this.tipoMovimiento = tipoMovimiento;
        this.tarjeta_id = tarjetaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFecha_create() {
        return fecha_create;
    }

    public void setFecha_create(Date fecha_create) {
        this.fecha_create = fecha_create;
    }

    public Date getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Date fecha_update) {
        this.fecha_update = fecha_update;
    }

    public String getTarjeta_id() {
        return tarjeta_id;
    }

    public void setTarjeta_id(String tarjeta_id) {
        this.tarjeta_id = tarjeta_id;
    }
}
