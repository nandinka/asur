package com.asur.modelos;

import java.util.ArrayList;
import java.util.List;

public class Perfil {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private List<Funcionalidad> funcionalidades;

    public Perfil() {
        this.funcionalidades = new ArrayList<>();
    }

    public Perfil(int id, String nombre, String descripcion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.funcionalidades = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<Funcionalidad> getFuncionalidades() { return funcionalidades; }
    public void setFuncionalidades(List<Funcionalidad> funcionalidades) { this.funcionalidades = funcionalidades; }

    public void agregarFuncionalidad(Funcionalidad f) {
        this.funcionalidades.add(f);
    }

    public boolean tieneFuncionalidad(String nombreFunc) {
        return funcionalidades.stream()
                .anyMatch(f -> f.getNombre().equals(nombreFunc));
    }

    @Override
    public String toString() {
        return String.format("Perfil[id=%d, nombre=%s, activo=%s]", id, nombre, activo);
    }
}
