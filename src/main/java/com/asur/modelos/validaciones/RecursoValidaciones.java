package com.asur.modelos.validaciones;

import java.math.BigDecimal;

public class RecursoValidaciones {

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "el nombre no puede estar vacio";
        if (nombre.length() < 3) return "el nombre debe tener al menos 3 caracteres";
        if (nombre.length() > 100) return "el nombre no puede tener mas de 100 caracteres";
        return null;
    }

    public static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) return null;
        if (descripcion.length() > 200) return "la descripcion no puede tener mas de 200 caracteres";
        return null;
    }

    public static String validarCapMax(int capMax) {
        if (capMax <= 0) return "la capacidad debe ser mayor a 0";
        if (capMax > 10000) return "la capacidad no puede ser mayor a 10000";
        return null;
    }

    public static String validarCostoHora(BigDecimal costoHora) {
        if (costoHora == null) return "el costo por hora no puede estar vacio";
        if (costoHora.compareTo(BigDecimal.ZERO) < 0) return "el costo por hora no puede ser negativo";
        return null;
    }
}
