package com.asur.daos.impl;

import com.asur.daos.interfaces.ReservaDAO;
import com.asur.modelos.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {
    private final Connection conn;
    public ReservaDAOImpl(Connection conn) { this.conn = conn; }

    public Reserva crearDesdeResultSet(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getInt("id_reserva"));
        r.setDatosContacto(rs.getString("datos_contacto"));
        r.setFecha(rs.getDate("fecha"));
        r.setHora(rs.getTime("hora"));
        r.setCantPersonas(rs.getInt("cant_personas"));
        r.setMontoTotal(rs.getBigDecimal("monto_total"));
        r.setDuracion(rs.getTime("duracion"));
        r.setImporteSena(rs.getBigDecimal("importe_sena"));
        r.setFechaVtoSena(rs.getDate("fecha_vto_sena"));
        r.setFechaPagoSena(rs.getDate("fecha_pago_sena"));
        r.setImporteSenaPagado(rs.getBigDecimal("importe_sena_pagado"));
        r.setIdEstadoPagoReserva(rs.getInt("id_estado_pago_reserva"));
        r.setIdUsuario(rs.getInt("id_usuario"));
        r.setIdRecurso(rs.getInt("id_recurso"));
        r.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        r.setActiva(rs.getBoolean("activa"));
        return r;
    }

    public Reserva obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reserva WHERE id_reserva = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Reserva> obtenerTodos() {
        List<Reserva> lista = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM reserva ORDER BY fecha DESC, hora DESC");
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Reserva> obtenerPorUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reserva WHERE id_usuario = ? ORDER BY fecha DESC")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Reserva> obtenerPorRecurso(int idRecurso) {
        List<Reserva> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reserva WHERE id_recurso = ? ORDER BY fecha DESC")) {
            ps.setInt(1, idRecurso);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Reserva> obtenerPorFecha(Date fecha) {
        List<Reserva> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reserva WHERE fecha = ? ORDER BY hora")) {
            ps.setDate(1, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Reserva> filtrar(Date fechaDesde, Date fechaHasta, Boolean activa, int[] idsRecurso) {
        List<Reserva> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM reserva WHERE 1=1");
        if (fechaDesde != null) sql.append(" AND fecha >= ?");
        if (fechaHasta != null) sql.append(" AND fecha <= ?");
        if (activa != null) sql.append(" AND activa = ?");
        sql.append(" ORDER BY fecha DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (fechaDesde != null) ps.setDate(idx++, fechaDesde);
            if (fechaHasta != null) ps.setDate(idx++, fechaHasta);
            if (activa != null) ps.setBoolean(idx++, activa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean existe(int id) { return obtenerPorId(id) != null; }

    public boolean hayConflicto(int idRecurso, Date fecha, Time hora, Time duracion) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM reserva WHERE id_recurso = ? AND fecha = ? AND activa = TRUE AND ((hora <= ? AND (hora + duracion) > ?) OR (hora < (? + ?) AND hora >= ?))")) {
            ps.setInt(1, idRecurso);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);
            ps.setTime(4, hora);
            ps.setTime(5, hora);
            ps.setTime(6, duracion);
            ps.setTime(7, hora);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public void insertar(Reserva r) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reserva (datos_contacto, fecha, hora, cant_personas, monto_total, duracion, importe_sena, fecha_vto_sena, fecha_pago_sena, importe_sena_pagado, id_estado_pago_reserva, id_usuario, id_recurso, activa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getDatosContacto());
            ps.setDate(2, r.getFecha());
            ps.setTime(3, r.getHora());
            ps.setInt(4, r.getCantPersonas());
            ps.setBigDecimal(5, r.getMontoTotal());
            ps.setTime(6, r.getDuracion());
            ps.setBigDecimal(7, r.getImporteSena());
            ps.setDate(8, r.getFechaVtoSena());
            ps.setDate(9, r.getFechaPagoSena());
            ps.setBigDecimal(10, r.getImporteSenaPagado());
            ps.setInt(11, r.getIdEstadoPagoReserva());
            ps.setInt(12, r.getIdUsuario());
            ps.setInt(13, r.getIdRecurso());
            ps.setBoolean(14, r.isActiva());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) r.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Reserva r) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE reserva SET datos_contacto=?, fecha=?, hora=?, cant_personas=?, monto_total=?, duracion=?, importe_sena=?, fecha_vto_sena=?, fecha_pago_sena=?, importe_sena_pagado=?, id_estado_pago_reserva=?, activa=? WHERE id_reserva=?")) {
            ps.setString(1, r.getDatosContacto());
            ps.setDate(2, r.getFecha());
            ps.setTime(3, r.getHora());
            ps.setInt(4, r.getCantPersonas());
            ps.setBigDecimal(5, r.getMontoTotal());
            ps.setTime(6, r.getDuracion());
            ps.setBigDecimal(7, r.getImporteSena());
            ps.setDate(8, r.getFechaVtoSena());
            ps.setDate(9, r.getFechaPagoSena());
            ps.setBigDecimal(10, r.getImporteSenaPagado());
            ps.setInt(11, r.getIdEstadoPagoReserva());
            ps.setBoolean(12, r.isActiva());
            ps.setInt(13, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void cancelar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE reserva SET activa = FALSE WHERE id_reserva = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
