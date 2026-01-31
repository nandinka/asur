package com.asur.daos.factory;

import com.asur.daos.impl.*;
import com.asur.daos.interfaces.*;
import com.asur.utils.ConexionDB;

import java.sql.Connection;

public class DAOFactory {
    private final Connection conn;

    public DAOFactory() {
        this.conn = ConexionDB.getInstancia().getConexion();
    }

    public UsuarioDAO getUsuarioDAO() { return new UsuarioDAOImpl(conn); }
    public PerfilDAO getPerfilDAO() { return new PerfilDAOImpl(conn); }
    public FuncionalidadDAO getFuncionalidadDAO() { return new FuncionalidadDAOImpl(conn); }
    public AuditoriaDAO getAuditoriaDAO() { return new AuditoriaDAOImpl(conn); }
    public TelefonoDAO getTelefonoDAO() { return new TelefonoDAOImpl(conn); }
    public RecursoDAO getRecursoDAO() { return new RecursoDAOImpl(conn); }
    public ReservaDAO getReservaDAO() { return new ReservaDAOImpl(conn); }
    public TipoActividadDAO getTipoActividadDAO() { return new TipoActividadDAOImpl(conn); }
    public ActividadDAO getActividadDAO() { return new ActividadDAOImpl(conn); }
    public InscripcionDAO getInscripcionDAO() { return new InscripcionDAOImpl(conn); }
    public PagoReservaDAO getPagoReservaDAO() { return new PagoReservaDAOImpl(conn); }
    public PagoActividadDAO getPagoActividadDAO() { return new PagoActividadDAOImpl(conn); }
    public PagoCuotaDAO getPagoCuotaDAO() { return new PagoCuotaDAOImpl(conn); }
    public CuotaDAO getCuotaDAO() { return new CuotaDAOImpl(conn); }
}
