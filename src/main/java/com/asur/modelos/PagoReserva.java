package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoReserva {
    private int id;
    private int idReserva;
    private BigDecimal monto;
    private Timestamp fechaPago;
    private int idFormaPago;
    private String observaciones;

    public PagoReserva() {}

    public PagoReserva(int id, int idReserva, BigDecimal monto, Timestamp fechaPago, int idFormaPago, String observaciones) {
        this.id = id;
        this.idReserva = idReserva;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.idFormaPago = idFormaPago;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
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
        return String.format("PagoReserva[id=%d, monto=%s]", id, monto);
    }
}
