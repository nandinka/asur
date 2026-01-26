package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cuota {
    private int id;
    private BigDecimal valorCuota;
    private int mes;
    private int anio;
    private int idEstadoPagoCuota;
    private int nroSocio;
    private int idCuotaBase;
    private Timestamp fechaGeneracion;

    public Cuota() {}

    public Cuota(int id, BigDecimal valorCuota, int mes, int anio, int idEstadoPagoCuota,
                 int nroSocio, int idCuotaBase) {
        this.id = id;
        this.valorCuota = valorCuota;
        this.mes = mes;
        this.anio = anio;
        this.idEstadoPagoCuota = idEstadoPagoCuota;
        this.nroSocio = nroSocio;
        this.idCuotaBase = idCuotaBase;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getValorCuota() { return valorCuota; }
    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }
    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getIdEstadoPagoCuota() { return idEstadoPagoCuota; }
    public void setIdEstadoPagoCuota(int idEstadoPagoCuota) { this.idEstadoPagoCuota = idEstadoPagoCuota; }
    public int getNroSocio() { return nroSocio; }
    public void setNroSocio(int nroSocio) { this.nroSocio = nroSocio; }
    public int getIdCuotaBase() { return idCuotaBase; }
    public void setIdCuotaBase(int idCuotaBase) { this.idCuotaBase = idCuotaBase; }
    public Timestamp getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Timestamp fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getPeriodo() {
        return String.format("%02d/%d", mes, anio);
    }

    @Override
    public String toString() {
        return String.format("Cuota[id=%d, periodo=%s, valor=%s]", id, getPeriodo(), valorCuota);
    }
}
