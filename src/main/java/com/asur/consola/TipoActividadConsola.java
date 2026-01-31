package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.TipoActividad;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.util.List;

public class TipoActividadConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public TipoActividadConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Tipos de Actividad",
                opcion("Listar tipos", this::listar),
                opcion("Crear tipo", this::crear),
                opcion("Modificar tipo", this::modificar),
                opcion("Activar/Desactivar tipo", this::cambiarEstado)
        );
    }

    private void listar() {
        List<TipoActividad> tipos = dao.getTipoActividadDAO().obtenerTodos();
        System.out.println("\n=== TIPOS DE ACTIVIDAD ===");
        for (TipoActividad t : tipos) {
            String estado = t.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("  %d - %s: %s [%s]%n", t.getId(), t.getNombre(), t.getDescripcion(), estado);
        }
    }

    private void crear() {
        System.out.println("\n=== CREAR TIPO DE ACTIVIDAD ===");
        String nombre = pedirString("Nombre: ", s -> s.length() < 3 ? "minimo 3 caracteres" : null);
        String descripcion = pedirStringOpcional("Descripcion");
        
        TipoActividad t = new TipoActividad();
        t.setNombre(nombre);
        t.setDescripcion(descripcion);
        t.setActivo(true);
        
        dao.getTipoActividadDAO().insertar(t);
        System.out.println("Tipo de actividad creado con ID: " + t.getId());
    }

    private void modificar() {
        int id = pedirInt("ID del tipo a modificar: ");
        TipoActividad t = dao.getTipoActividadDAO().obtenerPorId(id);
        if (t == null) {
            System.out.println("Tipo no encontrado.");
            return;
        }
        
        System.out.println("Modificando: " + t.getNombre());
        
        String nombre = pedirString("Nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) t.setNombre(nombre);
        
        String descripcion = pedirString("Nueva descripcion (enter para mantener): ");
        if (!descripcion.isBlank()) t.setDescripcion(descripcion);
        
        dao.getTipoActividadDAO().actualizar(t);
        System.out.println("Tipo actualizado.");
    }

    private void cambiarEstado() {
        int id = pedirInt("ID del tipo: ");
        TipoActividad t = dao.getTipoActividadDAO().obtenerPorId(id);
        if (t == null) {
            System.out.println("Tipo no encontrado.");
            return;
        }
        
        boolean nuevoEstado = !t.isActivo();
        t.setActivo(nuevoEstado);
        dao.getTipoActividadDAO().actualizar(t);
        System.out.println("Tipo " + (nuevoEstado ? "activado" : "desactivado") + ".");
    }
}
