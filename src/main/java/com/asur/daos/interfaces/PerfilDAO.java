package com.asur.daos.interfaces;

import com.asur.modelos.Perfil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PerfilDAO {
    Perfil crearDesdeResultSet(ResultSet rs) throws SQLException;
    Perfil obtenerPorId(int id);
    Perfil obtenerPorIdConFuncionalidades(int id);
    Perfil obtenerPorNombre(String nombre);
    List<Perfil> obtenerTodos();
    List<Perfil> obtenerActivos();
    boolean existe(int id);
    boolean existeNombre(String nombre);
    void insertar(Perfil perfil);
    void actualizar(Perfil perfil);
    void darBaja(int id);
    void asignarFuncionalidad(int idPerfil, int idFuncionalidad);
    void agregarFuncionalidad(int idPerfil, int idFuncionalidad);
    void quitarFuncionalidad(int idPerfil, int idFuncionalidad);
}
