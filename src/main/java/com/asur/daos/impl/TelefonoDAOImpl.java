package com.asur.daos.impl;

import com.asur.daos.interfaces.TelefonoDAO;
import com.asur.modelos.Telefono;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefonoDAOImpl implements TelefonoDAO {
    private final Connection conn;
    public TelefonoDAOImpl(Connection conn) { this.conn = conn; }

    public Telefono crearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Telefono(rs.getInt("id_telefono"), rs.getString("numero"), rs.getInt("id_tipo_telefono"), rs.getInt("id_estado_telefono"), rs.getInt("id_usuario"));
    }

    public List<Telefono> obtenerPorUsuario(int idUsuario) {
        List<Telefono> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM telefono WHERE id_usuario = ?")) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void insertar(Telefono t) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO telefono (numero, id_tipo_telefono, id_estado_telefono, id_usuario) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNumero());
            ps.setInt(2, t.getIdTipoTelefono());
            ps.setInt(3, t.getIdEstadoTelefono());
            ps.setInt(4, t.getIdUsuario());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) t.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Telefono t) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE telefono SET numero = ?, id_tipo_telefono = ?, id_estado_telefono = ? WHERE id_telefono = ?")) {
            ps.setString(1, t.getNumero());
            ps.setInt(2, t.getIdTipoTelefono());
            ps.setInt(3, t.getIdEstadoTelefono());
            ps.setInt(4, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM telefono WHERE id_telefono = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void desactivar(int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE telefono SET id_estado_telefono = 2 WHERE id_telefono = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
