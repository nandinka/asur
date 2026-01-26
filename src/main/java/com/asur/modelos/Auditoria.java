package com.asur.modelos;

import java.sql.Timestamp;

public class Auditoria {
    private int id;
    private Timestamp fechaHora;
    private int idUsuario;
    private int idFuncionalidad;
    private String operacion;
    private String detalle;
    private String ipCliente;

    public Auditoria() {}

    public Auditoria(int id, Timestamp fechaHora, int idUsuario, int idFuncionalidad,
                     String operacion, String detalle, String ipCliente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
        this.idFuncionalidad = idFuncionalidad;
        this.operacion = operacion;
        this.detalle = detalle;
        this.ipCliente = ipCliente;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getFechaHora() { return fechaHora; }
    public void setFechaHora(Timestamp fechaHora) { this.fechaHora = fechaHora; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdFuncionalidad() { return idFuncionalidad; }
    public void setIdFuncionalidad(int idFuncionalidad) { this.idFuncionalidad = idFuncionalidad; }
    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public String getIpCliente() { return ipCliente; }
    public void setIpCliente(String ipCliente) { this.ipCliente = ipCliente; }

    @Override
    public String toString() {
        return String.format("Auditoria[id=%d, fecha=%s, op=%s]", id, fechaHora, operacion);
    }
}
