package com.asur.daos.interfaces;

import com.asur.modelos.TipoActividad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TipoActividadDAO {
    TipoActividad crearDesdeResultSet(ResultSet rs) throws SQLException;
    TipoActividad obtenerPorId(int id);
    TipoActividad obtenerPorNombre(String nombre);
    List<TipoActividad> obtenerTodos();
    List<TipoActividad> obtenerActivos();
    boolean existe(int id);
    boolean existeNombre(String nombre);
    void insertar(TipoActividad tipoActividad);
    void actualizar(TipoActividad tipoActividad);
    void darBaja(int id);
}
