package com.asur.daos.impl;

import com.asur.daos.interfaces.InscripcionDAO;
import com.asur.modelos.InscripcionActividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAOImpl implements InscripcionDAO {
    private final Connection conn;
    public InscripcionDAOImpl(Connection conn) { this.conn = conn; }

    private InscripcionActividad crearDesdeResultSet(ResultSet rs) throws SQLException {
        InscripcionActividad i = new InscripcionActividad();
        i.setId(rs.getInt("id_inscripcion"));
        i.setIdUsuario(rs.getInt("id_usuario"));
        i.setIdActividad(rs.getInt("id_actividad"));
        i.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
        i.setActiva(rs.getBoolean("activa"));
        return i;
    }

    public InscripcionActividad obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inscripcion_actividad WHERE id_inscripcion = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<InscripcionActividad> obtenerPorUsuario(int idUsuario) {
        List<InscripcionActividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inscripcion_actividad WHERE id_usuario = ? ORDER BY fecha_inscripcion DESC")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<InscripcionActividad> obtenerPorActividad(int idActividad) {
        List<InscripcionActividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inscripcion_actividad WHERE id_actividad = ? ORDER BY fecha_inscripcion")) {
            ps.setInt(1, idActividad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean estaInscrito(int idUsuario, int idActividad) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM inscripcion_actividad WHERE id_usuario = ? AND id_actividad = ? AND activa = TRUE")) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idActividad);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public void insertar(InscripcionActividad i) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO inscripcion_actividad (id_usuario, id_actividad, fecha_inscripcion, activa) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdUsuario());
            ps.setInt(2, i.getIdActividad());
            ps.setDate(3, i.getFechaInscripcion());
            ps.setBoolean(4, i.isActiva());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) i.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void cancelar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inscripcion_actividad SET activa = FALSE WHERE id_inscripcion = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
