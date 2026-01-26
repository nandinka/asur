package com.asur.modelos.validaciones;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ActividadValidaciones {

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "el nombre no puede estar vacio";
        if (nombre.length() < 3) return "el nombre debe tener al menos 3 caracteres";
        if (nombre.length() > 100) return "el nombre no puede tener mas de 100 caracteres";
        return null;
    }

    public static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) return null;
        return null;
    }

    public static String validarFechaHora(Timestamp fechaHora) {
        if (fechaHora == null) return "la fecha y hora no pueden estar vacias";
        if (fechaHora.before(new Timestamp(System.currentTimeMillis()))) {
            return "la fecha y hora deben ser futuras";
        }
        return null;
    }

    public static String validarCostoTicket(BigDecimal costo) {
        if (costo == null) return null; // puede ser gratis
        if (costo.compareTo(BigDecimal.ZERO) < 0) return "el costo no puede ser negativo";
        return null;
    }

    public static String validarTipoActividad(int idTipo) {
        if (idTipo <= 0) return "debe seleccionar un tipo de actividad";
        return null;
    }
}
