package com.asur.modelos;

import java.sql.Timestamp;

public class Administrador {
    private int id;
    private int idUsuario;
    private String cargo;
    private Timestamp fechaAlta;
    private Usuario usuario;

    public Administrador() {}

    public Administrador(int id, int idUsuario, String cargo) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.cargo = cargo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Timestamp getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Timestamp fechaAlta) { this.fechaAlta = fechaAlta; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return String.format("Admin[id=%d, cargo=%s]", id, cargo);
    }
}
