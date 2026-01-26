package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.Telefono;
import com.asur.modelos.Usuario;
import com.asur.modelos.validaciones.UsuarioValidaciones;
import com.asur.servicios.UsuarioServicio;
import static com.asur.utils.Consola.*;
import java.util.List;

public class UsuarioConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;
    private final UsuarioServicio servicio;

    public UsuarioConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
        this.servicio = new UsuarioServicio(dao);
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Usuarios",
                opcion("Listar usuarios", this::listar),
                opcion("Buscar por nombre", this::buscarNombre),
                opcion("Registrar usuario", this::registrarUsuario),
                opcion("Modificar usuario", this::modificar),
                opcion("Dar de baja usuario", this::darBaja)
        );
    }

    public void registrarUsuario() {
        System.out.println("\n--- Registro de Usuario ---");
        
        String nombre = pedirString("nombre: ", UsuarioValidaciones::validarNombre);
        String apellido = pedirString("apellido: ", UsuarioValidaciones::validarApellido);
        
        int tipoDoc = pedirInt("tipo documento (1=CI, 2=Pasaporte, 3=DNI, 4=Otro): ");
        String documento = pedirString("documento: ", d -> UsuarioValidaciones.validarDocumento(d, tipoDoc));
        
        if (servicio.existeDocumento(documento)) {
            System.out.println("el documento ya esta registrado");
            return;
        }

        String correo = pedirString("correo: ", UsuarioValidaciones::validarCorreo);
        if (servicio.existeCorreo(correo)) {
            System.out.println("el correo ya esta registrado");
            return;
        }

        String contrasena = pedirString("contrasena: ", UsuarioValidaciones::validarContrasena);
        String calle = pedirStringOpcional("calle");
        String nroPuerta = pedirStringOpcional("nro puerta");
        
        int idPerfil = pedirInt("perfil (1=Admin, 2=Socio, 3=Auxiliar, 4=Invitado): ");

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setDocumento(documento);
        u.setIdTipoDocumento(tipoDoc);
        u.setCorreo(correo);
        u.setContrasena(contrasena);
        u.setCalle(calle);
        u.setNroPuerta(nroPuerta);
        u.setIdEstadoUsuario(1);
        u.setIdPerfil(idPerfil);

        servicio.registrar(u);

        // agregar telefono principal
        String telefono = pedirString("telefono principal: ", UsuarioValidaciones::validarTelefono);
        int tipoTel = pedirInt("tipo telefono (1=Celular, 2=Fijo, 3=Trabajo, 4=Otro): ");
        
        Telefono t = new Telefono();
        t.setNumero(telefono);
        t.setIdTipoTelefono(tipoTel);
        t.setIdEstadoTelefono(1);
        t.setIdUsuario(u.getId());
        servicio.agregarTelefono(t);

        // opcion de agregar telefono adicional
        if (pedirBoolean("desea agregar otro telefono? (si/no): ")) {
            String tel2 = pedirString("telefono adicional: ", UsuarioValidaciones::validarTelefono);
            int tipoTel2 = pedirInt("tipo telefono (1=Celular, 2=Fijo, 3=Trabajo, 4=Otro): ");
            Telefono t2 = new Telefono();
            t2.setNumero(tel2);
            t2.setIdTipoTelefono(tipoTel2);
            t2.setIdEstadoTelefono(1);
            t2.setIdUsuario(u.getId());
            servicio.agregarTelefono(t2);
        }

        System.out.println("usuario registrado con id: " + u.getId());
    }

    public void listar() {
        List<Usuario> usuarios = servicio.obtenerTodos();
        System.out.println("\n--- Usuarios ---");
        for (Usuario u : usuarios) {
            System.out.printf("%d - %s %s (%s)%n", u.getId(), u.getNombre(), u.getApellido(), u.getCorreo());
        }
    }

    public void buscarNombre() {
        String nombre = pedirString("nombre a buscar: ");
        List<Usuario> usuarios = servicio.filtrarPorNombre(nombre);
        for (Usuario u : usuarios) {
            System.out.printf("%d - %s %s (%s)%n", u.getId(), u.getNombre(), u.getApellido(), u.getCorreo());
        }
    }

    public void modificar() {
        int id = pedirInt("id del usuario a modificar: ");
        Usuario u = servicio.obtenerPorId(id);
        if (u == null) {
            System.out.println("usuario no encontrado");
            return;
        }

        System.out.println("modificando: " + u.getNombreCompleto());
        String nombre = pedirString("nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) u.setNombre(nombre);

        String apellido = pedirString("nuevo apellido (enter para mantener): ");
        if (!apellido.isBlank()) u.setApellido(apellido);

        servicio.actualizar(u);
        System.out.println("usuario actualizado");
    }

    public void darBaja() {
        int id = pedirInt("id del usuario a dar de baja: ");
        if (!servicio.existe(id)) {
            System.out.println("usuario no encontrado");
            return;
        }
        servicio.darBaja(id);
        System.out.println("usuario dado de baja");
    }

    public void modificarDatosPropios() {
        System.out.println("\n--- Modificar mis datos ---");
        System.out.println("datos actuales: " + sesion.getNombreCompleto());

        String nombre = pedirString("nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) sesion.setNombre(nombre);

        String apellido = pedirString("nuevo apellido (enter para mantener): ");
        if (!apellido.isBlank()) sesion.setApellido(apellido);

        String calle = pedirString("nueva calle (enter para mantener): ");
        if (!calle.isBlank()) sesion.setCalle(calle);

        servicio.actualizarDatosPropios(sesion);
        System.out.println("datos actualizados");
    }
}
