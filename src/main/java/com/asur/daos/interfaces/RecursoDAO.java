package com.asur.daos.interfaces;

import com.asur.modelos.Recurso;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RecursoDAO {
    Recurso crearDesdeResultSet(ResultSet rs) throws SQLException;
    Recurso obtenerPorId(int id);
    Recurso obtenerPorNombre(String nombre);
    List<Recurso> obtenerTodos();
    List<Recurso> obtenerActivos();
    boolean existe(int id);
    boolean existeNombre(String nombre);
    void insertar(Recurso recurso);
    void actualizar(Recurso recurso);
    void darBaja(int id);
}
