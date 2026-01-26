package com.asur.modelos.validaciones;

public class PerfilValidaciones {

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "el nombre no puede estar vacio";
        if (nombre.length() < 3) return "el nombre debe tener al menos 3 caracteres";
        if (nombre.length() > 50) return "el nombre no puede tener mas de 50 caracteres";
        return null;
    }

    public static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) return null; // opcional
        if (descripcion.length() > 200) return "la descripcion no puede tener mas de 200 caracteres";
        return null;
    }
}
