package com.asur.controlador;

import com.asur.consola.*;
import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Funcionalidad;
import com.asur.modelos.Perfil;
import com.asur.modelos.Usuario;
import com.asur.servicios.AuditoriaServicio;
import com.asur.servicios.UsuarioServicio;
import com.asur.utils.CifradoContrasenia;
import com.asur.utils.Consola;

import static com.asur.utils.Consola.*;

public class ControladorPrincipal {
    private final DAOFactory daoFactory;
    private final UsuarioServicio usuarioServicio;
    private final AuditoriaServicio auditoriaServicio;
    private Usuario sesion;
    private Perfil perfilSesion;

    public ControladorPrincipal() {
        this.daoFactory = new DAOFactory();
        this.usuarioServicio = new UsuarioServicio(daoFactory);
        this.auditoriaServicio = new AuditoriaServicio(daoFactory);
    }

    public void iniciar() {
        pedirSeleccion("Menu Principal",
                opcion("Iniciar Sesion", this::login),
                opcion("Registrarse", this::registrarNuevoUsuario)
        );
    }

    private void login() {
        String correo = pedirString("correo: ", s -> s.isBlank() ? "ingrese un correo" : null);
        String contrasenia = pedirString("contraseña: ", s -> s.isBlank() ? "ingrese una contraseña" : null);

        Usuario usuario = usuarioServicio.obtenerPorCorreo(correo);
        if (usuario == null) {
            System.out.println("usuario no encontrado");
            return;
        }

        if (usuario.getIdEstadoUsuario() != 2) {
            System.out.println("usuario inactivo o pendiente de validacion");
            return;
        }

        if (!CifradoContrasenia.getInstancia().verificar(contrasenia, usuario.getContrasena())) {
            System.out.println("contraseña incorrecta");
            return;
        }

        this.sesion = usuario;
        this.perfilSesion = usuarioServicio.obtenerPerfilConFuncionalidades(usuario.getIdPerfil());

        // registrar auditoria
        Funcionalidad funcLogin = daoFactory.getFuncionalidadDAO().obtenerPorNombre("RF001-05");
        if (funcLogin != null) {
            auditoriaServicio.registrar(sesion, funcLogin, "LOGIN", "Inicio de sesion exitoso");
        }

        System.out.println("\nbienvenido " + sesion.getNombre() + "!");
        menuPrincipal();
    }

    private void registrarNuevoUsuario() {
        UsuarioConsola usuarioConsola = new UsuarioConsola(daoFactory, null, null);
        usuarioConsola.registrarUsuario();
    }

    private void menuPrincipal() {
        var opciones = opciones();

        // agregar opciones segun funcionalidades del perfil
        if (tieneFuncionalidad("RF001-02") || tieneFuncionalidad("RF001-03") || tieneFuncionalidad("RF001-04")) {
            opciones.add(opcion("Gestion de Usuarios", this::menuUsuarios));
        }

        if (tieneFuncionalidad("RF002-01") || tieneFuncionalidad("RF002-02")) {
            opciones.add(opcion("Gestion de Perfiles", this::menuPerfiles));
        }

        if (tieneFuncionalidad("RF003-01") || tieneFuncionalidad("RF003-02")) {
            opciones.add(opcion("Gestion de Funcionalidades", this::menuFuncionalidades));
        }

        if (tieneFuncionalidad("RF004-01") || tieneFuncionalidad("RF004-02")) {
            opciones.add(opcion("Auditoria", this::menuAuditoria));
        }

        if (tieneFuncionalidad("RF005-01") || tieneFuncionalidad("RF005-02")) {
            opciones.add(opcion("Gestion de Actividades", this::menuActividades));
        }

        if (tieneFuncionalidad("RF006-01") || tieneFuncionalidad("RF006-02")) {
            opciones.add(opcion("Gestion de Espacios", this::menuEspacios));
        }

        if (tieneFuncionalidad("RF006-05")) {
            opciones.add(opcion("Reservas", this::menuReservas));
        }

        if (tieneFuncionalidad("RF007-01") || tieneFuncionalidad("RF007-02")) {
            opciones.add(opcion("Gestion de Tipos de Actividades", this::menuTiposActividad));
        }

        if (tieneFuncionalidad("RF008-01") || tieneFuncionalidad("RF008-02")) {
            opciones.add(opcion("Gestion de Pagos", this::menuPagos));
        }

        // siempre disponible
        opciones.add(opcion("Modificar mis datos", this::modificarDatosPropios));
        opciones.add(opcion("Cerrar Sesion", this::cerrarSesion));

        pedirSeleccion("Menu - " + perfilSesion.getNombre(), opciones);
    }

    private boolean tieneFuncionalidad(String nombre) {
        return perfilSesion != null && perfilSesion.tieneFuncionalidad(nombre);
    }

    private void menuUsuarios() {
        UsuarioConsola consola = new UsuarioConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuPerfiles() {
        PerfilConsola consola = new PerfilConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuFuncionalidades() {
        FuncionalidadConsola consola = new FuncionalidadConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuAuditoria() {
        AuditoriaConsola consola = new AuditoriaConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuActividades() {
        ActividadConsola consola = new ActividadConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuEspacios() {
        RecursoConsola consola = new RecursoConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuReservas() {
        ReservaConsola consola = new ReservaConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuTiposActividad() {
        TipoActividadConsola consola = new TipoActividadConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void menuPagos() {
        PagoConsola consola = new PagoConsola(daoFactory, sesion, perfilSesion);
        consola.mostrarMenu();
    }

    private void modificarDatosPropios() {
        UsuarioConsola consola = new UsuarioConsola(daoFactory, sesion, perfilSesion);
        consola.modificarDatosPropios();
    }

    private void cerrarSesion() {
        Funcionalidad funcLogout = daoFactory.getFuncionalidadDAO().obtenerPorNombre("RF001-05");
        if (funcLogout != null) {
            auditoriaServicio.registrar(sesion, funcLogout, "LOGOUT", "Cierre de sesion");
        }

        System.out.println("sesion cerrada");
        this.sesion = null;
        this.perfilSesion = null;
    }
}
