package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;

public class PerfilConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public PerfilConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        System.out.println("modulo PerfilConsola en desarrollo");
    }
}
