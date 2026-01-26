package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Date;

public class ValorCuotaBase {
    private int id;
    private BigDecimal costoCuota;
    private Date fechaVigencia;
    private boolean activo;

    public ValorCuotaBase() {}

    public ValorCuotaBase(int id, BigDecimal costoCuota, Date fechaVigencia, boolean activo) {
        this.id = id;
        this.costoCuota = costoCuota;
        this.fechaVigencia = fechaVigencia;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getCostoCuota() { return costoCuota; }
    public void setCostoCuota(BigDecimal costoCuota) { this.costoCuota = costoCuota; }
    public Date getFechaVigencia() { return fechaVigencia; }
    public void setFechaVigencia(Date fechaVigencia) { this.fechaVigencia = fechaVigencia; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("CuotaBase[id=%d, valor=%s, desde=%s]", id, costoCuota, fechaVigencia);
    }
}
