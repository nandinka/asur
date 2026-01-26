package com.asur.servicios;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Auditoria;
import com.asur.modelos.Funcionalidad;
import com.asur.modelos.Usuario;
import java.sql.Timestamp;
import java.util.List;

public class AuditoriaServicio {
    private final DAOFactory dao;

    public AuditoriaServicio(DAOFactory dao) { this.dao = dao; }

    public void registrar(Usuario usuario, Funcionalidad func, String operacion, String detalle) {
        Auditoria a = new Auditoria();
        a.setIdUsuario(usuario.getId());
        a.setIdFuncionalidad(func.getId());
        a.setOperacion(operacion);
        a.setDetalle(detalle);
        dao.getAuditoriaDAO().insertar(a);
    }

    public List<Auditoria> obtenerTodos() { return dao.getAuditoriaDAO().obtenerTodos(); }
    public List<Auditoria> obtenerPorUsuario(int id) { return dao.getAuditoriaDAO().obtenerPorUsuario(id); }
    public List<Auditoria> obtenerPorFechas(Timestamp desde, Timestamp hasta) { return dao.getAuditoriaDAO().obtenerPorFechas(desde, hasta); }
}
