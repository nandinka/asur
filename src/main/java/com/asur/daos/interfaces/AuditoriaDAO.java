package com.asur.daos.interfaces;

import com.asur.modelos.Auditoria;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface AuditoriaDAO {
    Auditoria crearDesdeResultSet(ResultSet rs) throws SQLException;
    void insertar(Auditoria auditoria);
    List<Auditoria> obtenerTodos();
    List<Auditoria> obtenerPorUsuario(int idUsuario);
    List<Auditoria> obtenerPorFechas(Timestamp desde, Timestamp hasta);
    List<Auditoria> obtenerPorOperacion(String operacion);
    List<Auditoria> obtenerPorFuncionalidad(int idFuncionalidad);
}
