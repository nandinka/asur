package com.asur.daos.impl;

import com.asur.daos.interfaces.CuotaDAO;
import com.asur.modelos.Cuota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuotaDAOImpl implements CuotaDAO {
    private final Connection conn;
    public CuotaDAOImpl(Connection conn) { this.conn = conn; }

    public Cuota obtenerPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM cuota WHERE id_cuota = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cuota c = new Cuota();
                c.setId(rs.getInt("id_cuota"));
                c.setIdUsuario(rs.getInt("nro_socio"));
                c.setPeriodo(rs.getInt("mes") + "/" + rs.getInt("anio"));
                c.setMonto(rs.getBigDecimal("valor_cuota"));
                c.setIdEstadoPago(rs.getInt("id_estado_pago_cuota"));
                return c;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Cuota> obtenerPendientesPorUsuario(int idUsuario) {
        List<Cuota> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM cuota WHERE nro_socio = ? AND id_estado_pago_cuota != 1 ORDER BY anio, mes")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cuota c = new Cuota();
                c.setId(rs.getInt("id_cuota"));
                c.setIdUsuario(rs.getInt("nro_socio"));
                c.setPeriodo(rs.getInt("mes") + "/" + rs.getInt("anio"));
                c.setMonto(rs.getBigDecimal("valor_cuota"));
                c.setIdEstadoPago(rs.getInt("id_estado_pago_cuota"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void marcarPagada(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE cuota SET id_estado_pago_cuota = 1 WHERE id_cuota = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
