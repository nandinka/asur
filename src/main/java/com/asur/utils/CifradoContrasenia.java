package com.asur.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CifradoContrasenia {
    private static CifradoContrasenia instancia = null;
    private final PasswordEncoder encoder;

    private CifradoContrasenia() {
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public static CifradoContrasenia getInstancia() {
        if (instancia == null) {
            instancia = new CifradoContrasenia();
        }
        return instancia;
    }

    public String cifrar(String contrasenia) {
        return encoder.encode(contrasenia);
    }

    public boolean verificar(String contraseniaPlana, String contraseniaCifrada) {
        return encoder.matches(contraseniaPlana, contraseniaCifrada);
    }
}
