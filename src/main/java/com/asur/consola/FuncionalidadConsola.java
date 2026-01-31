package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Funcionalidad;
import com.asur.modelos.Perfil;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.util.List;

public class FuncionalidadConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public FuncionalidadConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Funcionalidades",
                opcion("Listar funcionalidades", this::listar),
                opcion("Crear funcionalidad", this::crear),
                opcion("Modificar funcionalidad", this::modificar),
                opcion("Activar/Desactivar funcionalidad", this::cambiarEstado),
                opcion("Vincular a perfil", this::vincularAPerfil)
        );
    }

    private void listar() {
        List<Funcionalidad> funcs = dao.getFuncionalidadDAO().obtenerTodas();
        System.out.println("\n=== FUNCIONALIDADES ===");
        for (Funcionalidad f : funcs) {
            String estado = f.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("  %d - %s: %s [%s]%n", f.getId(), f.getNombre(), f.getDescripcion(), estado);
        }
    }

    private void crear() {
        System.out.println("\n=== CREAR FUNCIONALIDAD ===");
        String nombre = pedirString("Codigo (ej: RF001-07): ", s -> s.length() < 3 ? "minimo 3 caracteres" : null);
        String descripcion = pedirString("Descripcion: ", s -> s.isBlank() ? "requerido" : null);
        
        Funcionalidad f = new Funcionalidad();
        f.setNombre(nombre);
        f.setDescripcion(descripcion);
        f.setActivo(true);
        
        dao.getFuncionalidadDAO().insertar(f);
        System.out.println("Funcionalidad creada con ID: " + f.getId());
    }

    private void modificar() {
        int id = pedirInt("ID de la funcionalidad a modificar: ");
        Funcionalidad f = dao.getFuncionalidadDAO().obtenerPorId(id);
        if (f == null) {
            System.out.println("Funcionalidad no encontrada.");
            return;
        }
        
        System.out.println("Modificando: " + f.getNombre());
        
        String nombre = pedirString("Nuevo codigo (enter para mantener): ");
        if (!nombre.isBlank()) f.setNombre(nombre);
        
        String descripcion = pedirString("Nueva descripcion (enter para mantener): ");
        if (!descripcion.isBlank()) f.setDescripcion(descripcion);
        
        dao.getFuncionalidadDAO().actualizar(f);
        System.out.println("Funcionalidad actualizada.");
    }

    private void cambiarEstado() {
        int id = pedirInt("ID de la funcionalidad: ");
        Funcionalidad f = dao.getFuncionalidadDAO().obtenerPorId(id);
        if (f == null) {
            System.out.println("Funcionalidad no encontrada.");
            return;
        }
        
        boolean nuevoEstado = !f.isActivo();
        f.setActivo(nuevoEstado);
        dao.getFuncionalidadDAO().actualizar(f);
        System.out.println("Funcionalidad " + (nuevoEstado ? "activada" : "desactivada") + ".");
    }

    private void vincularAPerfil() {
        System.out.println("\nPerfiles disponibles:");
        List<Perfil> perfiles = dao.getPerfilDAO().obtenerTodos();
        for (Perfil p : perfiles) {
            System.out.printf("  %d - %s%n", p.getId(), p.getNombre());
        }
        
        int idPerfil = pedirInt("ID del perfil: ");
        if (dao.getPerfilDAO().obtenerPorId(idPerfil) == null) {
            System.out.println("Perfil no encontrado.");
            return;
        }
        
        System.out.println("\nFuncionalidades disponibles:");
        listar();
        
        int idFunc = pedirInt("ID de la funcionalidad a vincular: ");
        if (dao.getFuncionalidadDAO().obtenerPorId(idFunc) == null) {
            System.out.println("Funcionalidad no encontrada.");
            return;
        }
        
        dao.getPerfilDAO().agregarFuncionalidad(idPerfil, idFunc);
        System.out.println("Funcionalidad vinculada al perfil.");
    }
}
