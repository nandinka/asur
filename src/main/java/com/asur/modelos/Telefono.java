package com.asur.modelos;

public class Telefono {
    private int id;
    private String numero;
    private int idTipoTelefono;
    private int idEstadoTelefono;
    private int idUsuario;

    public Telefono() {}

    public Telefono(int id, String numero, int idTipoTelefono, int idEstadoTelefono, int idUsuario) {
        this.id = id;
        this.numero = numero;
        this.idTipoTelefono = idTipoTelefono;
        this.idEstadoTelefono = idEstadoTelefono;
        this.idUsuario = idUsuario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public int getIdTipoTelefono() { return idTipoTelefono; }
    public void setIdTipoTelefono(int idTipoTelefono) { this.idTipoTelefono = idTipoTelefono; }
    public int getIdEstadoTelefono() { return idEstadoTelefono; }
    public void setIdEstadoTelefono(int idEstadoTelefono) { this.idEstadoTelefono = idEstadoTelefono; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return numero;
    }
}
