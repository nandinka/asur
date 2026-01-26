package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoCuota {
    private int id;
    private int idCuota;
    private BigDecimal monto;
    private Timestamp fechaPago;
    private int idFormaPago;
    private String observaciones;

    public PagoCuota() {}

    public PagoCuota(int id, int idCuota, BigDecimal monto, Timestamp fechaPago, int idFormaPago, String observaciones) {
        this.id = id;
        this.idCuota = idCuota;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.idFormaPago = idFormaPago;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCuota() { return idCuota; }
    public void setIdCuota(int idCuota) { this.idCuota = idCuota; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }
    public int getIdFormaPago() { return idFormaPago; }
    public void setIdFormaPago(int idFormaPago) { this.idFormaPago = idFormaPago; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return String.format("PagoCuota[id=%d, monto=%s, fecha=%s]", id, monto, fechaPago);
    }
}
