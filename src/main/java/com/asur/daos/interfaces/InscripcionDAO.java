package com.asur.daos.interfaces;

import com.asur.modelos.InscripcionActividad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface InscripcionDAO {
    InscripcionActividad crearDesdeResultSet(ResultSet rs) throws SQLException;
    List<InscripcionActividad> obtenerPorActividad(int idActividad);
    List<InscripcionActividad> obtenerPorUsuario(int idUsuario);
    List<InscripcionActividad> filtrar(int[] idsTipoActividad, Timestamp fechaDesde, Timestamp fechaHasta, Boolean activa);
    boolean existe(int idActividad, int idUsuario);
    void insertar(InscripcionActividad inscripcion);
    void cancelar(int idActividad, int idUsuario);
}
