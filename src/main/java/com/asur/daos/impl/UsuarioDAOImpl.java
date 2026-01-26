package com.asur.daos.impl;

import com.asur.daos.interfaces.UsuarioDAO;
import com.asur.modelos.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final Connection conn;

    public UsuarioDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Usuario crearDesdeResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setDocumento(rs.getString("documento"));
        u.setIdTipoDocumento(rs.getInt("id_tipo_documento"));
        u.setCorreo(rs.getString("correo"));
        u.setCorreoConfirmado(rs.getBoolean("correo_confirmado"));
        u.setContrasena(rs.getString("contrasena"));
        u.setCalle(rs.getString("calle"));
        u.setNroPuerta(rs.getString("nro_puerta"));
        u.setApto(rs.getString("apto"));
        u.setIdEstadoUsuario(rs.getInt("id_estado_usuario"));
        u.setFecNacimiento(rs.getDate("fec_nacimiento"));
        u.setIdPerfil(rs.getInt("id_perfil"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return u;
    }

    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Usuario obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE LOWER(correo) = LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Usuario obtenerPorDocumento(String documento) {
        String sql = "SELECT * FROM usuario WHERE documento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return crearDesdeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY apellido, nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Usuario> filtrarPorNombre(String nombre) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Usuario> filtrarPorApellido(String apellido) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE LOWER(apellido) LIKE LOWER(?) ORDER BY apellido";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + apellido + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Usuario> filtrarPorEstado(int idEstado) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE id_estado_usuario = ? ORDER BY apellido, nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Usuario> filtrarPorPerfil(int idPerfil) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE id_perfil = ? ORDER BY apellido, nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerfil);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(crearDesdeResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean existe(int id) {
        String sql = "SELECT 1 FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM usuario WHERE LOWER(correo) = LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existeDocumento(String documento) {
        String sql = "SELECT 1 FROM usuario WHERE documento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documento);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void insertar(Usuario u) {
        String sql = """
            INSERT INTO usuario (nombre, apellido, documento, id_tipo_documento, correo, 
                correo_confirmado, contrasena, calle, nro_puerta, apto, id_estado_usuario, 
                fec_nacimiento, id_perfil) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDocumento());
            ps.setInt(4, u.getIdTipoDocumento());
            ps.setString(5, u.getCorreo());
            ps.setBoolean(6, u.isCorreoConfirmado());
            ps.setString(7, u.getContrasena());
            ps.setString(8, u.getCalle());
            ps.setString(9, u.getNroPuerta());
            ps.setString(10, u.getApto());
            ps.setInt(11, u.getIdEstadoUsuario());
            ps.setDate(12, u.getFecNacimiento());
            ps.setInt(13, u.getIdPerfil());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) u.setId(keys.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Usuario u) {
        String sql = """
            UPDATE usuario SET nombre = ?, apellido = ?, documento = ?, id_tipo_documento = ?,
                correo = ?, correo_confirmado = ?, calle = ?, nro_puerta = ?, apto = ?,
                id_estado_usuario = ?, fec_nacimiento = ?, id_perfil = ?
            WHERE id_usuario = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDocumento());
            ps.setInt(4, u.getIdTipoDocumento());
            ps.setString(5, u.getCorreo());
            ps.setBoolean(6, u.isCorreoConfirmado());
            ps.setString(7, u.getCalle());
            ps.setString(8, u.getNroPuerta());
            ps.setString(9, u.getApto());
            ps.setInt(10, u.getIdEstadoUsuario());
            ps.setDate(11, u.getFecNacimiento());
            ps.setInt(12, u.getIdPerfil());
            ps.setInt(13, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarDatosPropios(Usuario u) {
        String sql = """
            UPDATE usuario SET nombre = ?, apellido = ?, calle = ?, nro_puerta = ?, apto = ?,
                fec_nacimiento = ?
            WHERE id_usuario = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCalle());
            ps.setString(4, u.getNroPuerta());
            ps.setString(5, u.getApto());
            ps.setDate(6, u.getFecNacimiento());
            ps.setInt(7, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void darBaja(int id) {
        cambiarEstado(id, 2);
    }

    @Override
    public void cambiarEstado(int id, int nuevoEstado) {
        String sql = "UPDATE usuario SET id_estado_usuario = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
