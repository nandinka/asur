package com.asur.daos.impl;

import com.asur.daos.interfaces.TipoActividadDAO;
import com.asur.modelos.TipoActividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoActividadDAOImpl implements TipoActividadDAO {
    private final Connection conn;
    public TipoActividadDAOImpl(Connection conn) { this.conn = conn; }

    public TipoActividad crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new TipoActividad(rs.getInt("id_tipo_actividad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getBoolean("activo"));
    }

    public TipoActividad obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM tipo_actividad WHERE id_tipo_actividad = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public TipoActividad obtenerPorNombre(String nombre) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM tipo_actividad WHERE LOWER(nombre) = LOWER(?)")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<TipoActividad> obtenerTodos() {
        List<TipoActividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM tipo_actividad ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<TipoActividad> obtenerActivos() {
        List<TipoActividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM tipo_actividad WHERE activo = TRUE ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }
    public boolean existeNombre(String nombre) { return obtenerPorNombre(nombre) != null; }

    public void insertar(TipoActividad t) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO tipo_actividad (nombre, descripcion, activo) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setBoolean(3, t.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) t.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(TipoActividad t) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE tipo_actividad SET nombre = ?, descripcion = ?, activo = ? WHERE id_tipo_actividad = ?")) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setBoolean(3, t.isActivo());
            ps.setInt(4, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void darBaja(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE tipo_actividad SET activo = FALSE WHERE id_tipo_actividad = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
