package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Actividad {
    private int id;
    private String nombre;
    private String descripcion;
    private int idTipoActividad;
    private Date fecha;
    private Time hora;
    private Time duracion;
    private int cupoMax;
    private int inscritos;
    private BigDecimal costoSocio;
    private BigDecimal costoNoSocio;
    private Integer idRecurso;
    private String lugar;
    private boolean activa;

    public Actividad() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdTipoActividad() { return idTipoActividad; }
    public void setIdTipoActividad(int idTipoActividad) { this.idTipoActividad = idTipoActividad; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }
    public Time getDuracion() { return duracion; }
    public void setDuracion(Time duracion) { this.duracion = duracion; }
    public int getCupoMax() { return cupoMax; }
    public void setCupoMax(int cupoMax) { this.cupoMax = cupoMax; }
    public int getInscritos() { return inscritos; }
    public void setInscritos(int inscritos) { this.inscritos = inscritos; }
    public BigDecimal getCostoSocio() { return costoSocio; }
    public void setCostoSocio(BigDecimal costoSocio) { this.costoSocio = costoSocio; }
    public BigDecimal getCostoNoSocio() { return costoNoSocio; }
    public void setCostoNoSocio(BigDecimal costoNoSocio) { this.costoNoSocio = costoNoSocio; }
    public Integer getIdRecurso() { return idRecurso; }
    public void setIdRecurso(Integer idRecurso) { this.idRecurso = idRecurso; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public int getCuposDisponibles() { return cupoMax - inscritos; }

    @Override
    public String toString() {
        return String.format("Actividad[id=%d, nombre=%s, fecha=%s]", id, nombre, fecha);
    }
}
