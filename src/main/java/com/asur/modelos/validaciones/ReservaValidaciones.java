package com.asur.modelos.validaciones;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class ReservaValidaciones {

    public static String validarFecha(Date fecha) {
        if (fecha == null) return "la fecha no puede estar vacia";
        if (fecha.before(new Date(System.currentTimeMillis()))) {
            return "la fecha debe ser futura";
        }
        return null;
    }

    public static String validarHora(Time hora) {
        if (hora == null) return "la hora no puede estar vacia";
        return null;
    }

    public static String validarCantPersonas(int cantPersonas) {
        if (cantPersonas <= 0) return "la cantidad de personas debe ser mayor a 0";
        return null;
    }

    public static String validarDuracion(Time duracion) {
        if (duracion == null) return "la duracion no puede estar vacia";
        return null;
    }

    public static String validarMonto(BigDecimal monto) {
        if (monto == null) return "el monto no puede estar vacio";
        if (monto.compareTo(BigDecimal.ZERO) < 0) return "el monto no puede ser negativo";
        return null;
    }

    public static String validarRecurso(int idRecurso) {
        if (idRecurso <= 0) return "debe seleccionar un espacio";
        return null;
    }
}
