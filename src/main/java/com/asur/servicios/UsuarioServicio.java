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
        u.setIdEstadoUsuario(1); // Sin validar - pendiente de aprobacion
        dao.getUsuarioDAO().insertar(u);
        
        // intentar enviar email de confirmacion (no critico si falla)
        try {
            com.asur.utils.EmailSender.getInstancia().enviarEmail(
                u.getCorreo(),
                "ASUR - Registro en proceso de aprobacion",
                String.format(
                    "<html><body>" +
                    "<h2>Bienvenido a ASUR</h2>" +
                    "<p>Estimado/a %s %s,</p>" +
                    "<p>Su solicitud de registro ha sido recibida y está en proceso de aprobación.</p>" +
                    "<p>Su número de usuario es: <strong>%d</strong></p>" +
                    "<p>Un administrador validará sus datos y recibirá un correo cuando su cuenta esté activa.</p>" +
                    "<br><p>Saludos,<br>Asociación de Sordos del Uruguay</p>" +
                    "</body></html>",
                    u.getNombre(), u.getApellido(), u.getId()
                ),
                true
            );
        } catch (Exception e) {
            // email no configurado o error de envio - no es critico
            // el registro continua normalmente
        }
    }

    public void actualizar(Usuario u) { dao.getUsuarioDAO().actualizar(u); }
    public void actualizarDatosPropios(Usuario u) { dao.getUsuarioDAO().actualizarDatosPropios(u); }
    public void darBaja(int id) { dao.getUsuarioDAO().darBaja(id); }

    public List<Usuario> obtenerPendientesActivacion() {
        return dao.getUsuarioDAO().filtrarPorEstado(1); // Estado 1 = Sin validar
    }

    public int contarPendientesActivacion() {
        return obtenerPendientesActivacion().size();
    }

    public void activarUsuario(int id) {
        Usuario u = dao.getUsuarioDAO().obtenerPorId(id);
        if (u != null) {
            u.setIdEstadoUsuario(2); // Estado 2 = Activo
            dao.getUsuarioDAO().actualizar(u);
        }
    }

    public Perfil obtenerPerfilConFuncionalidades(int idPerfil) {
        return dao.getPerfilDAO().obtenerPorIdConFuncionalidades(idPerfil);
    }

    public List<Telefono> obtenerTelefonos(int idUsuario) {
        return dao.getTelefonoDAO().obtenerPorUsuario(idUsuario);
    }

    public void agregarTelefono(Telefono t) { dao.getTelefonoDAO().insertar(t); }
}
