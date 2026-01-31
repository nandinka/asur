package com.asur.daos.impl;

import com.asur.daos.interfaces.PagoCuotaDAO;
import com.asur.modelos.PagoCuota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoCuotaDAOImpl implements PagoCuotaDAO {
    private final Connection conn;
    public PagoCuotaDAOImpl(Connection conn) { this.conn = conn; }

    public void insertar(PagoCuota p) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pago_cuota (id_cuota, monto, fecha_pago, id_forma_pago) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdCuota());
            ps.setBigDecimal(2, p.getMonto());
            ps.setTimestamp(3, p.getFechaPago());
            ps.setInt(4, p.getIdFormaPago());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<PagoCuota> obtenerPorCuota(int idCuota) {
        List<PagoCuota> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM pago_cuota WHERE id_cuota = ?")) {
            ps.setInt(1, idCuota);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoCuota p = new PagoCuota();
                p.setId(rs.getInt("id_pago_cuota"));
                p.setIdCuota(rs.getInt("id_cuota"));
                p.setMonto(rs.getBigDecimal("monto"));
                p.setFechaPago(rs.getTimestamp("fecha_pago"));
                p.setIdFormaPago(rs.getInt("id_forma_pago"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
