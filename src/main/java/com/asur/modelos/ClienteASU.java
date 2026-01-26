package com.asur.modelos;

import java.sql.Timestamp;

public class ClienteASU {
    private int nroSocio;
    private int idUsuario;
    private boolean dificultadAuditiva;
    private int idNivelLSU;
    private int idCategoria;
    private int idDescuento;
    private boolean estadoSocio;
    private Timestamp fechaAltaSocio;
    private Usuario usuario;

    public ClienteASU() {}

    public ClienteASU(int nroSocio, int idUsuario, boolean dificultadAuditiva, int idNivelLSU,
                      int idCategoria, int idDescuento, boolean estadoSocio) {
        this.nroSocio = nroSocio;
        this.idUsuario = idUsuario;
        this.dificultadAuditiva = dificultadAuditiva;
        this.idNivelLSU = idNivelLSU;
        this.idCategoria = idCategoria;
        this.idDescuento = idDescuento;
        this.estadoSocio = estadoSocio;
    }

    public int getNroSocio() { return nroSocio; }
    public void setNroSocio(int nroSocio) { this.nroSocio = nroSocio; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public boolean isDificultadAuditiva() { return dificultadAuditiva; }
    public void setDificultadAuditiva(boolean dificultadAuditiva) { this.dificultadAuditiva = dificultadAuditiva; }
    public int getIdNivelLSU() { return idNivelLSU; }
    public void setIdNivelLSU(int idNivelLSU) { this.idNivelLSU = idNivelLSU; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public int getIdDescuento() { return idDescuento; }
    public void setIdDescuento(int idDescuento) { this.idDescuento = idDescuento; }
    public boolean isEstadoSocio() { return estadoSocio; }
    public void setEstadoSocio(boolean estadoSocio) { this.estadoSocio = estadoSocio; }
    public Timestamp getFechaAltaSocio() { return fechaAltaSocio; }
    public void setFechaAltaSocio(Timestamp fechaAltaSocio) { this.fechaAltaSocio = fechaAltaSocio; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return String.format("Socio[nro=%d, activo=%s]", nroSocio, estadoSocio);
    }
}
