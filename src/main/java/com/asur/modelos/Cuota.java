package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Cuota {
    private int id;
    private int idUsuario;
    private String periodo;
    private BigDecimal monto;
    private Date fechaVencimiento;
    private int idEstadoPago;
    private Timestamp fechaGeneracion;

    public Cuota() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public int getIdEstadoPago() { return idEstadoPago; }
    public void setIdEstadoPago(int idEstadoPago) { this.idEstadoPago = idEstadoPago; }
    public Timestamp getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Timestamp fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}
