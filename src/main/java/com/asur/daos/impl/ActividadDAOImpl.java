package com.asur.daos.impl;

import com.asur.daos.interfaces.ActividadDAO;
import com.asur.modelos.Actividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAOImpl implements ActividadDAO {
    private final Connection conn;
    public ActividadDAOImpl(Connection conn) { this.conn = conn; }

    private Actividad crearDesdeResultSet(ResultSet rs) throws SQLException {
        Actividad a = new Actividad();
        a.setId(rs.getInt("id_actividad"));
        a.setNombre(rs.getString("nombre"));
        a.setDescripcion(rs.getString("descripcion"));
        a.setIdTipoActividad(rs.getInt("id_tipo_actividad"));
        a.setFecha(rs.getDate("fecha"));
        a.setHora(rs.getTime("hora"));
        a.setDuracion(rs.getTime("duracion"));
        a.setCupoMax(rs.getInt("cupo_max"));
        a.setInscritos(rs.getInt("inscritos"));
        a.setCostoSocio(rs.getBigDecimal("costo_socio"));
        a.setCostoNoSocio(rs.getBigDecimal("costo_no_socio"));
        a.setIdRecurso(rs.getObject("id_recurso") != null ? rs.getInt("id_recurso") : null);
        a.setLugar(rs.getString("lugar"));
        a.setActiva(rs.getBoolean("activa"));
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

    public List<Actividad> obtenerTodas() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM actividad ORDER BY fecha DESC, hora");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerActivas() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM actividad WHERE activa = TRUE ORDER BY fecha, hora");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> obtenerDisponibles() {
        List<Actividad> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                "SELECT * FROM actividad WHERE activa = TRUE AND fecha >= CURRENT_DATE AND inscritos < cupo_max ORDER BY fecha, hora");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Actividad> filtrar(Date desde, Date hasta, Integer idTipo) {
        List<Actividad> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM actividad WHERE 1=1");
        if (desde != null) sql.append(" AND fecha >= ?");
        if (hasta != null) sql.append(" AND fecha <= ?");
        if (idTipo != null) sql.append(" AND id_tipo_actividad = ?");
        sql.append(" ORDER BY fecha DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (desde != null) ps.setDate(idx++, desde);
            if (hasta != null) ps.setDate(idx++, hasta);
            if (idTipo != null) ps.setInt(idx++, idTipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void insertar(Actividad a) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO actividad (nombre, descripcion, id_tipo_actividad, fecha, hora, duracion, cupo_max, inscritos, costo_socio, costo_no_socio, id_recurso, lugar, activa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setInt(3, a.getIdTipoActividad());
            ps.setDate(4, a.getFecha());
            ps.setTime(5, a.getHora());
            ps.setTime(6, a.getDuracion());
            ps.setInt(7, a.getCupoMax());
            ps.setInt(8, a.getInscritos());
            ps.setBigDecimal(9, a.getCostoSocio());
            ps.setBigDecimal(10, a.getCostoNoSocio());
            if (a.getIdRecurso() != null) ps.setInt(11, a.getIdRecurso());
            else ps.setNull(11, Types.INTEGER);
            ps.setString(12, a.getLugar());
            ps.setBoolean(13, a.isActiva());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) a.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Actividad a) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE actividad SET nombre=?, descripcion=?, id_tipo_actividad=?, fecha=?, hora=?, duracion=?, cupo_max=?, costo_socio=?, costo_no_socio=?, lugar=?, activa=? WHERE id_actividad=?")) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setInt(3, a.getIdTipoActividad());
            ps.setDate(4, a.getFecha());
            ps.setTime(5, a.getHora());
            ps.setTime(6, a.getDuracion());
            ps.setInt(7, a.getCupoMax());
            ps.setBigDecimal(8, a.getCostoSocio());
            ps.setBigDecimal(9, a.getCostoNoSocio());
            ps.setString(10, a.getLugar());
            ps.setBoolean(11, a.isActiva());
            ps.setInt(12, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void cancelar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE actividad SET activa = FALSE WHERE id_actividad = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void incrementarInscritos(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE actividad SET inscritos = inscritos + 1 WHERE id_actividad = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void decrementarInscritos(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE actividad SET inscritos = inscritos - 1 WHERE id_actividad = ? AND inscritos > 0")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
