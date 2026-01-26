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
        int pendientes = servicio.contarPendientesActivacion();
        String textoActivar = String.format("USUARIOS A ACTIVAR (%d)", pendientes);
        
        pedirSeleccion("Gestion de Usuarios",
                opcion(textoActivar, this::activarUsuarios),
                opcion("Listar usuarios", this::listar),
                opcion("Buscar por nombre", this::buscarNombre),
                opcion("Registrar usuario", this::registrarUsuario),
                opcion("Modificar usuario", this::modificar),
                opcion("Dar de baja usuario", this::darBaja)
        );
    }

    public void activarUsuarios() {
        List<Usuario> pendientes = servicio.obtenerPendientesActivacion();
        
        if (pendientes.isEmpty()) {
            System.out.println("\nNo hay usuarios pendientes de activacion.");
            return;
        }
        
        do {
            System.out.println("\n=== USUARIOS PENDIENTES DE ACTIVACION ===");
            for (Usuario u : pendientes) {
                System.out.printf("  ID: %d - %s %s (%s)%n", 
                    u.getId(), u.getNombre(), u.getApellido(), u.getCorreo());
            }
            System.out.println();
            
            if (!pedirBoolean("¿Desea activar un usuario? (si/no): ")) {
                break;
            }
            
            int id = pedirInt("Introduzca el ID del usuario a activar: ");
            Usuario u = servicio.obtenerPorId(id);
            
            if (u == null) {
                System.out.println("Usuario no encontrado.");
            } else if (u.getIdEstadoUsuario() != 1) {
                System.out.println("El usuario no esta pendiente de activacion.");
            } else {
                servicio.activarUsuario(id);
                System.out.println("¡Usuario " + u.getNombreCompleto() + " activado exitosamente!");
                pendientes = servicio.obtenerPendientesActivacion();
                
                if (pendientes.isEmpty()) {
                    System.out.println("\nNo quedan usuarios pendientes de activacion.");
                    break;
                }
            }
            
        } while (pedirBoolean("¿Desea activar mas usuarios? (si/no): "));
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

        String contrasenia = pedirString("contraseña: ", UsuarioValidaciones::validarContrasena);
        String calle = pedirStringOpcional("calle");
        String nroPuerta = pedirStringOpcional("nro puerta");
        
        // solo se puede registrar como Socio (2) o No Socio/Invitado (4)
        // Admin (1) y Auxiliar (3) solo los puede asignar un administrador
        System.out.println("tipo de usuario:");
        System.out.println("  1. Socio");
        System.out.println("  2. No Socio");
        int tipoUsuario = pedirInt("seleccione (1-2): ", id -> (id >= 1 && id <= 2) ? null : "opcion invalida");
        int idPerfil = (tipoUsuario == 1) ? 2 : 4; // Socio=2, Invitado=4

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setDocumento(documento);
        u.setIdTipoDocumento(tipoDoc);
        u.setCorreo(correo);
        u.setContrasena(contrasenia);
        u.setCalle(calle);
        u.setNroPuerta(nroPuerta);
        u.setIdEstadoUsuario(1); // Sin validar
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

        System.out.println("\n¡Registro exitoso! ID: " + u.getId());
        System.out.println("Su solicitud sera revisada por un administrador.");
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
