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
    private BigDecimal importeSena;
    private Date fechaVtoSena;
    private Date fechaPagoSena;
    private BigDecimal importeSenaPagado;
    private int idEstadoPagoReserva;
    private int idUsuario;
    private int idRecurso;
    private Timestamp fechaRegistro;
    private boolean activa;

    public Reserva() {}

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
    public BigDecimal getImporteSena() { return importeSena; }
    public void setImporteSena(BigDecimal importeSena) { this.importeSena = importeSena; }
    public Date getFechaVtoSena() { return fechaVtoSena; }
    public void setFechaVtoSena(Date fechaVtoSena) { this.fechaVtoSena = fechaVtoSena; }
    public Date getFechaPagoSena() { return fechaPagoSena; }
    public void setFechaPagoSena(Date fechaPagoSena) { this.fechaPagoSena = fechaPagoSena; }
    public BigDecimal getImporteSenaPagado() { return importeSenaPagado; }
    public void setImporteSenaPagado(BigDecimal importeSenaPagado) { this.importeSenaPagado = importeSenaPagado; }
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

    public BigDecimal getSaldoPendiente() {
        if (montoTotal == null) return BigDecimal.ZERO;
        if (importeSenaPagado == null) return montoTotal;
        return montoTotal.subtract(importeSenaPagado);
    }

    @Override
    public String toString() {
        return String.format("Reserva[id=%d, fecha=%s, hora=%s, saldo=%s]", id, fecha, hora, getSaldoPendiente());
    }
}
