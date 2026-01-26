package com.asur.daos.impl;

import com.asur.daos.interfaces.RecursoDAO;
import com.asur.modelos.Recurso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAOImpl implements RecursoDAO {
    private final Connection conn;
    public RecursoDAOImpl(Connection conn) { this.conn = conn; }

    public Recurso crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Recurso(rs.getInt("id_recurso"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("cap_max"), rs.getBigDecimal("costo_hora"), rs.getBoolean("activo"));
    }

    public Recurso obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM recurso WHERE id_recurso = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Recurso obtenerPorNombre(String nombre) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM recurso WHERE LOWER(nombre) = LOWER(?)")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Recurso> obtenerTodos() {
        List<Recurso> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM recurso ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Recurso> obtenerActivos() {
        List<Recurso> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM recurso WHERE activo = TRUE ORDER BY nombre");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }
    public boolean existeNombre(String nombre) { return obtenerPorNombre(nombre) != null; }

    public void insertar(Recurso r) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO recurso (nombre, descripcion, cap_max, costo_hora, activo) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setInt(3, r.getCapMax());
            ps.setBigDecimal(4, r.getCostoHora());
            ps.setBoolean(5, r.isActivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) r.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Recurso r) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE recurso SET nombre = ?, descripcion = ?, cap_max = ?, costo_hora = ?, activo = ? WHERE id_recurso = ?")) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setInt(3, r.getCapMax());
            ps.setBigDecimal(4, r.getCostoHora());
            ps.setBoolean(5, r.isActivo());
            ps.setInt(6, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void darBaja(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE recurso SET activo = FALSE WHERE id_recurso = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
