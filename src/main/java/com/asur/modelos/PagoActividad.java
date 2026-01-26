package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoActividad {
    private int idActividad;
    private int idUsuario;
    private BigDecimal monto;
    private Timestamp fechaPago;
    private int idFormaPago;
    private int idEstadoPagoActividad;

    public PagoActividad() {}

    public PagoActividad(int idActividad, int idUsuario, BigDecimal monto, Timestamp fechaPago,
                         int idFormaPago, int idEstadoPagoActividad) {
        this.idActividad = idActividad;
        this.idUsuario = idUsuario;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.idFormaPago = idFormaPago;
        this.idEstadoPagoActividad = idEstadoPagoActividad;
    }

    public int getIdActividad() { return idActividad; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }
    public int getIdFormaPago() { return idFormaPago; }
    public void setIdFormaPago(int idFormaPago) { this.idFormaPago = idFormaPago; }
    public int getIdEstadoPagoActividad() { return idEstadoPagoActividad; }
    public void setIdEstadoPagoActividad(int idEstadoPagoActividad) { this.idEstadoPagoActividad = idEstadoPagoActividad; }

    @Override
    public String toString() {
        return String.format("PagoActividad[actividad=%d, usuario=%d, monto=%s]", idActividad, idUsuario, monto);
    }
}
