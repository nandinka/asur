package com.asur.daos.interfaces;

import com.asur.modelos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {
    Usuario crearDesdeResultSet(ResultSet rs) throws SQLException;
    Usuario obtenerPorId(int id);
    Usuario obtenerPorCorreo(String correo);
    Usuario obtenerPorDocumento(String documento);
    List<Usuario> obtenerTodos();
    List<Usuario> filtrarPorNombre(String nombre);
    List<Usuario> filtrarPorApellido(String apellido);
    List<Usuario> filtrarPorEstado(int idEstado);
    List<Usuario> filtrarPorPerfil(int idPerfil);
    boolean existe(int id);
    boolean existeCorreo(String correo);
    boolean existeDocumento(String documento);
    void insertar(Usuario usuario);
    void actualizar(Usuario usuario);
    void actualizarDatosPropios(Usuario usuario);
    void darBaja(int id);
    void cambiarEstado(int id, int nuevoEstado);
}
