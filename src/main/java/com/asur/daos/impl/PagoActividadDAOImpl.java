package com.asur.daos.impl;

import com.asur.daos.interfaces.PagoActividadDAO;
import com.asur.modelos.PagoActividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoActividadDAOImpl implements PagoActividadDAO {
    private final Connection conn;
    public PagoActividadDAOImpl(Connection conn) { this.conn = conn; }

    public void insertar(PagoActividad p) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pago_actividad (id_inscripcion, monto, fecha_pago, id_forma_pago, id_estado_pago) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdInscripcion());
            ps.setBigDecimal(2, p.getMonto());
            ps.setTimestamp(3, p.getFechaPago());
            ps.setInt(4, p.getIdFormaPago());
            ps.setInt(5, p.getIdEstadoPago());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<PagoActividad> obtenerPorInscripcion(int idInscripcion) {
        List<PagoActividad> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM pago_actividad WHERE id_inscripcion = ? ORDER BY fecha_pago")) {
            ps.setInt(1, idInscripcion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoActividad p = new PagoActividad();
                p.setId(rs.getInt("id_pago_actividad"));
                p.setIdInscripcion(rs.getInt("id_inscripcion"));
                p.setMonto(rs.getBigDecimal("monto"));
                p.setFechaPago(rs.getTimestamp("fecha_pago"));
                p.setIdFormaPago(rs.getInt("id_forma_pago"));
                p.setIdEstadoPago(rs.getInt("id_estado_pago"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
