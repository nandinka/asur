package com.asur.modelos;

import java.math.BigDecimal;

public class Recurso {
    private int id;
    private String nombre;
    private String descripcion;
    private int capMax;
    private BigDecimal costoHora;
    private boolean activo;

    public Recurso() {}

    public Recurso(int id, String nombre, String descripcion, int capMax, BigDecimal costoHora, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.capMax = capMax;
        this.costoHora = costoHora;
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
    public BigDecimal getCostoHora() { return costoHora; }
    public void setCostoHora(BigDecimal costoHora) { this.costoHora = costoHora; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("Recurso[id=%d, nombre=%s, cap=%d]", id, nombre, capMax);
    }
}
