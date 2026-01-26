package com.asur.daos.impl;

import com.asur.daos.interfaces.FuncionalidadDAO;
import com.asur.modelos.Funcionalidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionalidadDAOImpl implements FuncionalidadDAO {
    private final Connection conn;
    public FuncionalidadDAOImpl(Connection conn) { this.conn = conn; }

    public Funcionalidad crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Funcionalidad(rs.getInt("id_funcionalidad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getBoolean("activo"));
    }

    public Funcionalidad obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM funcionalidad WHERE id_funcionalidad = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Funcionalidad obtenerPorNombre(String nombre) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM funcionalidad WHERE nombre = ?")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Funcionalidad> obtenerTodos() {
        List<Funcionalidad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM funcionalidad ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Funcionalidad> obtenerActivos() {
        List<Funcionalidad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM funcionalidad WHERE activo = TRUE ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Funcionalidad> obtenerPorPerfil(int idPerfil) {
        List<Funcionalidad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT f.* FROM funcionalidad f JOIN perfil_funcionalidad pf ON f.id_funcionalidad = pf.id_funcionalidad WHERE pf.id_perfil = ?")) {
            ps.setInt(1, idPerfil);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }

    public void insertar(Funcionalidad f) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO funcionalidad (nombre, descripcion, activo) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNombre());
            ps.setString(2, f.getDescripcion());
            ps.setBoolean(3, f.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) f.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Funcionalidad f) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE funcionalidad SET nombre = ?, descripcion = ?, activo = ? WHERE id_funcionalidad = ?")) {
            ps.setString(1, f.getNombre());
            ps.setString(2, f.getDescripcion());
            ps.setBoolean(3, f.isActivo());
            ps.setInt(4, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void darBaja(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE funcionalidad SET activo = FALSE WHERE id_funcionalidad = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
