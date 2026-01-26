package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Date;

public class Recurso {
    private int id;
    private String nombre;
    private String descripcion;
    private int capMax;
    private BigDecimal costoHoraSocio;
    private BigDecimal costoHoraNoSocio;
    private Date fechaVigenciaPrecios;
    private boolean activo;

    public Recurso() {}

    public Recurso(int id, String nombre, String descripcion, int capMax, 
                   BigDecimal costoHoraSocio, BigDecimal costoHoraNoSocio, 
                   Date fechaVigenciaPrecios, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.capMax = capMax;
        this.costoHoraSocio = costoHoraSocio;
        this.costoHoraNoSocio = costoHoraNoSocio;
        this.fechaVigenciaPrecios = fechaVigenciaPrecios;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCapMax() { return capMax; }
    public void setCapMax(int capMax) { this.capMax = capMax; }
    public BigDecimal getCostoHoraSocio() { return costoHoraSocio; }
    public void setCostoHoraSocio(BigDecimal costoHoraSocio) { this.costoHoraSocio = costoHoraSocio; }
    public BigDecimal getCostoHoraNoSocio() { return costoHoraNoSocio; }
    public void setCostoHoraNoSocio(BigDecimal costoHoraNoSocio) { this.costoHoraNoSocio = costoHoraNoSocio; }
    public Date getFechaVigenciaPrecios() { return fechaVigenciaPrecios; }
    public void setFechaVigenciaPrecios(Date fechaVigenciaPrecios) { this.fechaVigenciaPrecios = fechaVigenciaPrecios; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("Recurso[id=%d, nombre=%s, cap=%d, socio=$%s, noSocio=$%s]", 
                id, nombre, capMax, costoHoraSocio, costoHoraNoSocio);
    }
}
