package com.asur.daos.impl;

import com.asur.daos.interfaces.InscripcionDAO;
import com.asur.modelos.InscripcionActividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAOImpl implements InscripcionDAO {
    private final Connection conn;
    public InscripcionDAOImpl(Connection conn) { this.conn = conn; }

    public InscripcionActividad crearDesdeResultSet(ResultSet rs) throws SQLException {
        InscripcionActividad i = new InscripcionActividad();
        i.setIdActividad(rs.getInt("id_actividad"));
        i.setIdUsuario(rs.getInt("id_usuario"));
        i.setFechaInscripcion(rs.getTimestamp("fecha_inscripcion"));
        i.setActiva(rs.getBoolean("activa"));
        i.setFechaCancelacion(rs.getTimestamp("fecha_cancelacion"));
        return i;
    }

    public List<InscripcionActividad> obtenerPorActividad(int idActividad) {
        List<InscripcionActividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inscripcion_actividad WHERE id_actividad = ?")) {
            ps.setInt(1, idActividad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<InscripcionActividad> obtenerPorUsuario(int idUsuario) {
        List<InscripcionActividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inscripcion_actividad WHERE id_usuario = ?")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<InscripcionActividad> filtrar(int[] idsTipo, Timestamp desde, Timestamp hasta, Boolean activa) {
        List<InscripcionActividad> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT ia.* FROM inscripcion_actividad ia JOIN actividad a ON ia.id_actividad = a.id_actividad WHERE 1=1");
        if (desde != null) sql.append(" AND ia.fecha_inscripcion >= ?");
        if (hasta != null) sql.append(" AND ia.fecha_inscripcion <= ?");
        if (activa != null) sql.append(" AND ia.activa = ?");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (desde != null) ps.setTimestamp(idx++, desde);
            if (hasta != null) ps.setTimestamp(idx++, hasta);
            if (activa != null) ps.setBoolean(idx++, activa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int idActividad, int idUsuario) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM inscripcion_actividad WHERE id_actividad = ? AND id_usuario = ?")) {
            ps.setInt(1, idActividad);
            ps.setInt(2, idUsuario);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public void insertar(InscripcionActividad i) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO inscripcion_actividad (id_actividad, id_usuario, activa) VALUES (?, ?, ?)")) {
            ps.setInt(1, i.getIdActividad());
            ps.setInt(2, i.getIdUsuario());
            ps.setBoolean(3, i.isActiva());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void cancelar(int idActividad, int idUsuario) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE inscripcion_actividad SET activa = FALSE, fecha_cancelacion = CURRENT_TIMESTAMP WHERE id_actividad = ? AND id_usuario = ?")) {
            ps.setInt(1, idActividad);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
