package com.asur.daos.impl;

import com.asur.daos.interfaces.PerfilDAO;
import com.asur.modelos.Funcionalidad;
import com.asur.modelos.Perfil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilDAOImpl implements PerfilDAO {
    private final Connection conn;
    public PerfilDAOImpl(Connection conn) { this.conn = conn; }

    public Perfil crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Perfil(rs.getInt("id_perfil"), rs.getString("nombre"), rs.getString("descripcion"), rs.getBoolean("activo"));
    }

    public Perfil obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM perfil WHERE id_perfil = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Perfil obtenerPorIdConFuncionalidades(int id) {
        Perfil p = obtenerPorId(id);
        if (p != null) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT f.* FROM funcionalidad f JOIN perfil_funcionalidad pf ON f.id_funcionalidad = pf.id_funcionalidad WHERE pf.id_perfil = ?")) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) p.agregarFuncionalidad(new Funcionalidad(rs.getInt("id_funcionalidad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getBoolean("activo")));
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return p;
    }

    public Perfil obtenerPorNombre(String nombre) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM perfil WHERE LOWER(nombre) = LOWER(?)")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Perfil> obtenerTodos() {
        List<Perfil> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM perfil ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Perfil> obtenerActivos() {
        List<Perfil> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM perfil WHERE activo = TRUE ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }
    public boolean existeNombre(String nombre) { return obtenerPorNombre(nombre) != null; }

    public void insertar(Perfil p) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO perfil (nombre, descripcion, activo) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBoolean(3, p.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Perfil p) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE perfil SET nombre = ?, descripcion = ?, activo = ? WHERE id_perfil = ?")) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBoolean(3, p.isActivo());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void darBaja(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE perfil SET activo = FALSE WHERE id_perfil = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void asignarFuncionalidad(int idPerfil, int idFunc) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO perfil_funcionalidad (id_perfil, id_funcionalidad) VALUES (?, ?) ON CONFLICT DO NOTHING")) {
            ps.setInt(1, idPerfil);
            ps.setInt(2, idFunc);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void quitarFuncionalidad(int idPerfil, int idFunc) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM perfil_funcionalidad WHERE id_perfil = ? AND id_funcionalidad = ?")) {
            ps.setInt(1, idPerfil);
            ps.setInt(2, idFunc);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
