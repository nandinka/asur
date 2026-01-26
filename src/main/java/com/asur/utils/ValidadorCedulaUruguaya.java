package com.asur.utils;

public class ValidadorCedulaUruguaya {

    public static String limpiarCedula(String ci) {
        if (ci == null) return "";
        return ci.replaceAll("\\D", "");
    }

    public static int calcularDigitoVerificador(String ci) {
        ci = limpiarCedula(ci);
        
        // completar con ceros a la izquierda si es menor a 7 digitos
        while (ci.length() < 7) {
            ci = "0" + ci;
        }
        
        // coeficientes para calculo
        String coeficientes = "2987634";
        int suma = 0;
        
        for (int i = 0; i < 7; i++) {
            int coef = Character.getNumericValue(coeficientes.charAt(i));
            int digito = Character.getNumericValue(ci.charAt(i));
            suma += (coef * digito) % 10;
        }
        
        if (suma % 10 == 0) {
            return 0;
        } else {
            return 10 - (suma % 10);
        }
    }

    public static boolean validarCedula(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            return false;
        }
        
        ci = limpiarCedula(ci);
        
        // debe tener al menos 6 digitos y maximo 8
        if (ci.length() < 6 || ci.length() > 8) {
            return false;
        }
        
        // rechazar cedulas con todos ceros
        if (ci.matches("^0+$")) {
            return false;
        }
        
        // la parte sin digito verificador no puede ser todos ceros
        String ciSinDigito = ci.substring(0, ci.length() - 1);
        if (ciSinDigito.matches("^0+$")) {
            return false;
        }
        
        // convertir a numero y validar rango minimo realista
        // las cedulas uruguayas validas empiezan desde aprox 100.000
        long valorNumerico = Long.parseLong(ciSinDigito);
        if (valorNumerico < 100000) {
            return false;
        }
        
        // obtener digito verificador ingresado
        int digitoIngresado = Character.getNumericValue(ci.charAt(ci.length() - 1));
        
        // calcular digito verificador
        int digitoCalculado = calcularDigitoVerificador(ciSinDigito);
        
        return digitoIngresado == digitoCalculado;
    }

    public static String generarCedulaAleatoria() {
        // generar numero aleatorio de 7 digitos
        int numero = (int) (Math.random() * 10000000);
        String ci = String.format("%07d", numero);
        
        // calcular y agregar digito verificador
        int digito = calcularDigitoVerificador(ci);
        
        return ci + digito;
    }

    public static String formatearCedula(String ci) {
        ci = limpiarCedula(ci);
        
        if (ci.length() < 7 || ci.length() > 8) {
            return ci;
        }
        
        // formato: X.XXX.XXX-X
        if (ci.length() == 7) {
            return ci.substring(0, 1) + "." + ci.substring(1, 4) + "." + ci.substring(4, 7) + "-" + ci.charAt(6);
        } else {
            return ci.substring(0, 1) + "." + ci.substring(1, 4) + "." + ci.substring(4, 7) + "-" + ci.charAt(7);
        }
    }
}
