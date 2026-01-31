package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoActividad {
    private int id;
    private int idInscripcion;
    private BigDecimal monto;
    private Timestamp fechaPago;
    private int idFormaPago;
    private int idEstadoPago;

    public PagoActividad() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }
    public int getIdFormaPago() { return idFormaPago; }
    public void setIdFormaPago(int idFormaPago) { this.idFormaPago = idFormaPago; }
    public int getIdEstadoPago() { return idEstadoPago; }
    public void setIdEstadoPago(int idEstadoPago) { this.idEstadoPago = idEstadoPago; }
}
