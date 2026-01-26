package com.asur.modelos.validaciones;

import com.asur.utils.ValidadorCedulaUruguaya;
import java.util.regex.Pattern;

public class UsuarioValidaciones {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "el nombre no puede estar vacio";
        if (nombre.length() < 2) return "el nombre debe tener al menos 2 caracteres";
        if (nombre.length() > 50) return "el nombre no puede tener mas de 50 caracteres";
        if (!NOMBRE_PATTERN.matcher(nombre).matches()) return "el nombre solo puede contener letras";
        return null;
    }

    public static String validarApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) return "el apellido no puede estar vacio";
        if (apellido.length() < 2) return "el apellido debe tener al menos 2 caracteres";
        if (apellido.length() > 50) return "el apellido no puede tener mas de 50 caracteres";
        if (!NOMBRE_PATTERN.matcher(apellido).matches()) return "el apellido solo puede contener letras";
        return null;
    }

    public static String validarDocumento(String documento, int tipoDocumento) {
        if (documento == null || documento.isBlank()) return "el documento no puede estar vacio";
        if (documento.length() > 20) return "el documento no puede tener mas de 20 caracteres";
        
        // si es cedula uruguaya (tipo 1), validar con algoritmo
        if (tipoDocumento == 1) {
            if (!ValidadorCedulaUruguaya.validarCedula(documento)) {
                return "la cedula uruguaya es invalida";
            }
        }
        return null;
    }

    public static String validarCorreo(String correo) {
        if (correo == null || correo.isBlank()) return "el correo no puede estar vacio";
        if (correo.length() > 100) return "el correo no puede tener mas de 100 caracteres";
        if (!EMAIL_PATTERN.matcher(correo).matches()) return "el formato del correo es invalido";
        return null;
    }

    public static String validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isBlank()) return "la contrasena no puede estar vacia";
        if (contrasena.length() < 8) return "la contrasena debe tener al menos 8 caracteres";
        if (contrasena.length() > 50) return "la contrasena no puede tener mas de 50 caracteres";
        if (!contrasena.matches(".*[a-zA-Z].*")) return "la contrasena debe tener al menos una letra";
        if (!contrasena.matches(".*\\d.*")) return "la contrasena debe tener al menos un numero";
        if (!contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "la contrasena debe tener al menos un caracter especial";
        }
        return null;
    }

    public static String validarCalle(String calle) {
        if (calle == null || calle.isBlank()) return null; // opcional
        if (calle.length() > 100) return "la calle no puede tener mas de 100 caracteres";
        return null;
    }

    public static String validarNroPuerta(String nroPuerta) {
        if (nroPuerta == null || nroPuerta.isBlank()) return null; // opcional
        if (nroPuerta.length() > 10) return "el numero de puerta no puede tener mas de 10 caracteres";
        return null;
    }

    public static String validarApto(String apto) {
        if (apto == null || apto.isBlank()) return null; // opcional
        if (apto.length() > 10) return "el apartamento no puede tener mas de 10 caracteres";
        return null;
    }

    public static String validarTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) return "el telefono no puede estar vacio";
        if (telefono.length() < 8) return "el telefono debe tener al menos 8 digitos";
        if (telefono.length() > 20) return "el telefono no puede tener mas de 20 digitos";
        if (!telefono.matches("\\d+")) return "el telefono solo puede contener numeros";
        return null;
    }
}
