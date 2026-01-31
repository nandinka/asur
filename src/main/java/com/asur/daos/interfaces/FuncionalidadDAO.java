package com.asur.daos.interfaces;

import com.asur.modelos.Funcionalidad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FuncionalidadDAO {
    Funcionalidad crearDesdeResultSet(ResultSet rs) throws SQLException;
    Funcionalidad obtenerPorId(int id);
    Funcionalidad obtenerPorNombre(String nombre);
    List<Funcionalidad> obtenerTodos();
    List<Funcionalidad> obtenerTodas();
    List<Funcionalidad> obtenerActivos();
    List<Funcionalidad> obtenerPorPerfil(int idPerfil);
    boolean existe(int id);
    void insertar(Funcionalidad funcionalidad);
    void actualizar(Funcionalidad funcionalidad);
    void darBaja(int id);
}
