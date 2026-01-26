package com.asur.daos.impl;

import com.asur.daos.interfaces.ActividadDAO;
import com.asur.modelos.Actividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAOImpl implements ActividadDAO {
    private final Connection conn;
    public ActividadDAOImpl(Connection conn) { this.conn = conn; }

    public Actividad crearDesdeResultSet(ResultSet rs) throws SQLException {
        Actividad a = new Actividad();
        a.setId(rs.getInt("id_actividad"));
        a.setNombre(rs.getString("nombre"));
        a.setFechaHoraActividad(rs.getTimestamp("fecha_hora_actividad"));
        a.setIdRecurso(rs.getObject("id_recurso") != null ? rs.getInt("id_recurso") : null);
        a.setCostoTicket(rs.getBigDecimal("costo_ticket"));
        a.setObservaciones(rs.getString("observaciones"));
        a.setDescripcion(rs.getString("descripcion"));
        a.setFechaHoraInicioInsc(rs.getTimestamp("fecha_hora_inicio_insc"));
        a.setRequiereInscripcion(rs.getBoolean("requiere_inscripcion"));
        a.setIdTipoActividad(rs.getInt("id_tipo_actividad"));
        a.setIdAdministrador(rs.getInt("id_administrador"));
        a.setEstado(rs.getBoolean("estado"));
        a.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return a;
    }

    public Actividad obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM actividad WHERE id_actividad = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Actividad> obtenerTodos() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM actividad ORDER BY fecha_hora_actividad DESC");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerActivos() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM actividad WHERE estado = TRUE ORDER BY fecha_hora_actividad");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerPorTipo(int idTipo) {
        List<Actividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM actividad WHERE id_tipo_actividad = ? ORDER BY fecha_hora_actividad")) {
            ps.setInt(1, idTipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerPorFecha(Timestamp fecha) {
        List<Actividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM actividad WHERE DATE(fecha_hora_actividad) = DATE(?) ORDER BY fecha_hora_actividad")) {
            ps.setTimestamp(1, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerDisponiblesParaInscripcion() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM actividad WHERE estado = TRUE AND requiere_inscripcion = TRUE AND fecha_hora_actividad > CURRENT_TIMESTAMP ORDER BY fecha_hora_actividad");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }

    public void insertar(Actividad a) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO actividad (nombre, fecha_hora_actividad, id_recurso, costo_ticket, observaciones, descripcion, fecha_hora_inicio_insc, requiere_inscripcion, id_tipo_actividad, id_administrador, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre());
            ps.setTimestamp(2, a.getFechaHoraActividad());
            if (a.getIdRecurso() != null) ps.setInt(3, a.getIdRecurso()); else ps.setNull(3, Types.INTEGER);
            ps.setBigDecimal(4, a.getCostoTicket());
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getDescripcion());
            ps.setTimestamp(7, a.getFechaHoraInicioInsc());
            ps.setBoolean(8, a.isRequiereInscripcion());
            ps.setInt(9, a.getIdTipoActividad());
            ps.setInt(10, a.getIdAdministrador());
            ps.setBoolean(11, a.isEstado());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) a.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Actividad a) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE actividad SET nombre = ?, fecha_hora_actividad = ?, id_recurso = ?, costo_ticket = ?, observaciones = ?, descripcion = ?, fecha_hora_inicio_insc = ?, requiere_inscripcion = ?, id_tipo_actividad = ?, estado = ? WHERE id_actividad = ?")) {
            ps.setString(1, a.getNombre());
            ps.setTimestamp(2, a.getFechaHoraActividad());
            if (a.getIdRecurso() != null) ps.setInt(3, a.getIdRecurso()); else ps.setNull(3, Types.INTEGER);
            ps.setBigDecimal(4, a.getCostoTicket());
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getDescripcion());
            ps.setTimestamp(7, a.getFechaHoraInicioInsc());
            ps.setBoolean(8, a.isRequiereInscripcion());
            ps.setInt(9, a.getIdTipoActividad());
            ps.setBoolean(10, a.isEstado());
            ps.setInt(11, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void darBaja(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE actividad SET estado = FALSE WHERE id_actividad = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
