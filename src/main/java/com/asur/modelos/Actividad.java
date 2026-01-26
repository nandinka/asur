package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;

public class Actividad {
    private int id;
    private String nombre;
    private Timestamp fechaHoraActividad;
    private Integer idRecurso;
    private Duration duracion;
    private BigDecimal costoTicket;
    private String observaciones;
    private String descripcion;
    private Timestamp fechaHoraInicioInsc;
    private Duration plazoInscripcion;
    private boolean requiereInscripcion;
    private int idTipoActividad;
    private int idAdministrador;
    private boolean estado;
    private Timestamp fechaRegistro;

    public Actividad() {}

    public Actividad(int id, String nombre, Timestamp fechaHoraActividad, Integer idRecurso,
                     Duration duracion, BigDecimal costoTicket, String observaciones, String descripcion,
                     Timestamp fechaHoraInicioInsc, Duration plazoInscripcion, boolean requiereInscripcion,
                     int idTipoActividad, int idAdministrador, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHoraActividad = fechaHoraActividad;
        this.idRecurso = idRecurso;
        this.duracion = duracion;
        this.costoTicket = costoTicket;
        this.observaciones = observaciones;
        this.descripcion = descripcion;
        this.fechaHoraInicioInsc = fechaHoraInicioInsc;
        this.plazoInscripcion = plazoInscripcion;
        this.requiereInscripcion = requiereInscripcion;
        this.idTipoActividad = idTipoActividad;
        this.idAdministrador = idAdministrador;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Timestamp getFechaHoraActividad() { return fechaHoraActividad; }
    public void setFechaHoraActividad(Timestamp fechaHoraActividad) { this.fechaHoraActividad = fechaHoraActividad; }
    public Integer getIdRecurso() { return idRecurso; }
    public void setIdRecurso(Integer idRecurso) { this.idRecurso = idRecurso; }
    public Duration getDuracion() { return duracion; }
    public void setDuracion(Duration duracion) { this.duracion = duracion; }
    public BigDecimal getCostoTicket() { return costoTicket; }
    public void setCostoTicket(BigDecimal costoTicket) { this.costoTicket = costoTicket; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Timestamp getFechaHoraInicioInsc() { return fechaHoraInicioInsc; }
    public void setFechaHoraInicioInsc(Timestamp fechaHoraInicioInsc) { this.fechaHoraInicioInsc = fechaHoraInicioInsc; }
    public Duration getPlazoInscripcion() { return plazoInscripcion; }
    public void setPlazoInscripcion(Duration plazoInscripcion) { this.plazoInscripcion = plazoInscripcion; }
    public boolean isRequiereInscripcion() { return requiereInscripcion; }
    public void setRequiereInscripcion(boolean requiereInscripcion) { this.requiereInscripcion = requiereInscripcion; }
    public int getIdTipoActividad() { return idTipoActividad; }
    public void setIdTipoActividad(int idTipoActividad) { this.idTipoActividad = idTipoActividad; }
    public int getIdAdministrador() { return idAdministrador; }
    public void setIdAdministrador(int idAdministrador) { this.idAdministrador = idAdministrador; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("Actividad[id=%d, nombre=%s, fecha=%s]", id, nombre, fechaHoraActividad);
    }
}
