package com.asur.servicios;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.Telefono;
import com.asur.modelos.Usuario;
import com.asur.utils.CifradoContrasenia;
import java.util.List;

public class UsuarioServicio {
    private final DAOFactory dao;

    public UsuarioServicio(DAOFactory dao) { this.dao = dao; }

    public Usuario obtenerPorId(int id) { return dao.getUsuarioDAO().obtenerPorId(id); }
    public Usuario obtenerPorCorreo(String correo) { return dao.getUsuarioDAO().obtenerPorCorreo(correo); }
    public List<Usuario> obtenerTodos() { return dao.getUsuarioDAO().obtenerTodos(); }
    public List<Usuario> filtrarPorNombre(String nombre) { return dao.getUsuarioDAO().filtrarPorNombre(nombre); }
    public List<Usuario> filtrarPorApellido(String apellido) { return dao.getUsuarioDAO().filtrarPorApellido(apellido); }
    public List<Usuario> filtrarPorEstado(int estado) { return dao.getUsuarioDAO().filtrarPorEstado(estado); }
    public List<Usuario> filtrarPorPerfil(int perfil) { return dao.getUsuarioDAO().filtrarPorPerfil(perfil); }
    public boolean existe(int id) { return dao.getUsuarioDAO().existe(id); }
    public boolean existeCorreo(String correo) { return dao.getUsuarioDAO().existeCorreo(correo); }
    public boolean existeDocumento(String doc) { return dao.getUsuarioDAO().existeDocumento(doc); }

    public void registrar(Usuario u) {
        u.setContrasena(CifradoContrasenia.getInstancia().cifrar(u.getContrasena()));
        dao.getUsuarioDAO().insertar(u);
    }

    public void actualizar(Usuario u) { dao.getUsuarioDAO().actualizar(u); }
    public void actualizarDatosPropios(Usuario u) { dao.getUsuarioDAO().actualizarDatosPropios(u); }
    public void darBaja(int id) { dao.getUsuarioDAO().darBaja(id); }

    public Perfil obtenerPerfilConFuncionalidades(int idPerfil) {
        return dao.getPerfilDAO().obtenerPorIdConFuncionalidades(idPerfil);
    }

    public List<Telefono> obtenerTelefonos(int idUsuario) {
        return dao.getTelefonoDAO().obtenerPorUsuario(idUsuario);
    }

    public void agregarTelefono(Telefono t) { dao.getTelefonoDAO().insertar(t); }
}
