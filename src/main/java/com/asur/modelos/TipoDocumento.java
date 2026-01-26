package com.asur.modelos;

import com.asur.utils.ValidadorCedulaUruguaya;

public class TipoDocumento {
    private int id;
    private String descripcion;

    public TipoDocumento() {}

    public TipoDocumento(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public static boolean esCedulaValida(String cedula) {
        return ValidadorCedulaUruguaya.validarCedula(cedula);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
