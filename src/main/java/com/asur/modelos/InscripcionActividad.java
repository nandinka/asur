package com.asur.modelos;

import java.sql.Timestamp;

public class InscripcionActividad {
    private int idActividad;
    private int idUsuario;
    private Timestamp fechaInscripcion;
    private boolean activa;
    private Timestamp fechaCancelacion;

    public InscripcionActividad() {}

    public InscripcionActividad(int idActividad, int idUsuario, Timestamp fechaInscripcion, boolean activa) {
        this.idActividad = idActividad;
        this.idUsuario = idUsuario;
        this.fechaInscripcion = fechaInscripcion;
        this.activa = activa;
    }

    public int getIdActividad() { return idActividad; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public Timestamp getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(Timestamp fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Timestamp getFechaCancelacion() { return fechaCancelacion; }
    public void setFechaCancelacion(Timestamp fechaCancelacion) { this.fechaCancelacion = fechaCancelacion; }

    @Override
    public String toString() {
        return String.format("Inscripcion[actividad=%d, usuario=%d, activa=%s]", idActividad, idUsuario, activa);
    }
}
