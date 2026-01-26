package com.asur.daos.interfaces;

import com.asur.modelos.Actividad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ActividadDAO {
    Actividad crearDesdeResultSet(ResultSet rs) throws SQLException;
    Actividad obtenerPorId(int id);
    List<Actividad> obtenerTodos();
    List<Actividad> obtenerActivos();
    List<Actividad> obtenerPorTipo(int idTipo);
    List<Actividad> obtenerPorFecha(Timestamp fecha);
    List<Actividad> obtenerDisponiblesParaInscripcion();
    boolean existe(int id);
    void insertar(Actividad actividad);
    void actualizar(Actividad actividad);
    void darBaja(int id);
}
