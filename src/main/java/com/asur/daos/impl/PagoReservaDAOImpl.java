package com.asur.daos.impl;

import com.asur.daos.interfaces.PagoReservaDAO;
import com.asur.modelos.PagoReserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoReservaDAOImpl implements PagoReservaDAO {
    private final Connection conn;
    public PagoReservaDAOImpl(Connection conn) { this.conn = conn; }

    public void insertar(PagoReserva p) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pago_reserva (id_reserva, monto, fecha_pago, id_forma_pago) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdReserva());
            ps.setBigDecimal(2, p.getMonto());
            ps.setTimestamp(3, p.getFechaPago());
            ps.setInt(4, p.getIdFormaPago());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<PagoReserva> obtenerPorReserva(int idReserva) {
        List<PagoReserva> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM pago_reserva WHERE id_reserva = ? ORDER BY fecha_pago")) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoReserva p = new PagoReserva();
                p.setId(rs.getInt("id_pago_reserva"));
                p.setIdReserva(rs.getInt("id_reserva"));
                p.setMonto(rs.getBigDecimal("monto"));
                p.setFechaPago(rs.getTimestamp("fecha_pago"));
                p.setIdFormaPago(rs.getInt("id_forma_pago"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
