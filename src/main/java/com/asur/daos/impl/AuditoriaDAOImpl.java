package com.asur.daos.impl;

import com.asur.daos.interfaces.AuditoriaDAO;
import com.asur.modelos.Auditoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAOImpl implements AuditoriaDAO {
    private final Connection conn;
    public AuditoriaDAOImpl(Connection conn) { this.conn = conn; }

    public Auditoria crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Auditoria(rs.getInt("id_auditoria"), rs.getTimestamp("fecha_hora"), rs.getInt("id_usuario"), rs.getInt("id_funcionalidad"), rs.getString("operacion"), rs.getString("detalle"), rs.getString("ip_cliente"));
    }

    public void insertar(Auditoria a) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO auditoria (id_usuario, id_funcionalidad, operacion, detalle, ip_cliente) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, a.getIdUsuario());
            ps.setInt(2, a.getIdFuncionalidad());
            ps.setString(3, a.getOperacion());
            ps.setString(4, a.getDetalle());
            ps.setString(5, a.getIpCliente());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Auditoria> obtenerTodos() {
        List<Auditoria> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM auditoria ORDER BY fecha_hora DESC");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerUltimos(int cantidad) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria ORDER BY fecha_hora DESC LIMIT ?")) {
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerPorUsuario(int idUsuario) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria WHERE id_usuario = ? ORDER BY fecha_hora DESC")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerPorFechas(Timestamp desde, Timestamp hasta) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria WHERE fecha_hora BETWEEN ? AND ? ORDER BY fecha_hora DESC")) {
            ps.setTimestamp(1, desde);
            ps.setTimestamp(2, hasta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerPorRangoFecha(Date desde, Date hasta) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria WHERE DATE(fecha_hora) BETWEEN ? AND ? ORDER BY fecha_hora DESC")) {
            ps.setDate(1, desde);
            ps.setDate(2, hasta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerPorOperacion(String operacion) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria WHERE operacion = ? ORDER BY fecha_hora DESC")) {
            ps.setString(1, operacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Auditoria> obtenerPorFuncionalidad(int idFunc) {
        List<Auditoria> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditoria WHERE id_funcionalidad = ? ORDER BY fecha_hora DESC")) {
            ps.setInt(1, idFunc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
