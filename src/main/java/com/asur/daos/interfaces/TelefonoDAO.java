package com.asur.daos.interfaces;

import com.asur.modelos.Telefono;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TelefonoDAO {
    Telefono crearDesdeResultSet(ResultSet rs) throws SQLException;
    List<Telefono> obtenerPorUsuario(int idUsuario);
    void insertar(Telefono telefono);
    void actualizar(Telefono telefono);
    void eliminar(int id);
    void desactivar(int id);
}
