package com.asur.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Reserva {
    private int id;
    private String datosContacto;
    private Date fecha;
    private Time hora;
    private int cantPersonas;
    private BigDecimal montoTotal;
    private Time duracion;
    private int idEstadoPagoReserva;
    private int idUsuario;
    private int idRecurso;
    private Timestamp fechaRegistro;
    private boolean activa;

    public Reserva() {}

    public Reserva(int id, String datosContacto, Date fecha, Time hora, int cantPersonas,
                   BigDecimal montoTotal, Time duracion, int idEstadoPagoReserva,
                   int idUsuario, int idRecurso, boolean activa) {
        this.id = id;
        this.datosContacto = datosContacto;
        this.fecha = fecha;
        this.hora = hora;
        this.cantPersonas = cantPersonas;
        this.montoTotal = montoTotal;
        this.duracion = duracion;
        this.idEstadoPagoReserva = idEstadoPagoReserva;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.activa = activa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDatosContacto() { return datosContacto; }
    public void setDatosContacto(String datosContacto) { this.datosContacto = datosContacto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }
    public int getCantPersonas() { return cantPersonas; }
    public void setCantPersonas(int cantPersonas) { this.cantPersonas = cantPersonas; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public Time getDuracion() { return duracion; }
    public void setDuracion(Time duracion) { this.duracion = duracion; }
    public int getIdEstadoPagoReserva() { return idEstadoPagoReserva; }
    public void setIdEstadoPagoReserva(int idEstadoPagoReserva) { this.idEstadoPagoReserva = idEstadoPagoReserva; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdRecurso() { return idRecurso; }
    public void setIdRecurso(int idRecurso) { this.idRecurso = idRecurso; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    @Override
    public String toString() {
        return String.format("Reserva[id=%d, fecha=%s, hora=%s]", id, fecha, hora);
    }
}
