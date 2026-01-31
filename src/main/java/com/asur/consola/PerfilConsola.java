package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Funcionalidad;
import com.asur.modelos.Perfil;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.util.List;

public class PerfilConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public PerfilConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Perfiles",
                opcion("Listar perfiles", this::listar),
                opcion("Ver detalle de perfil", this::verDetalle),
                opcion("Crear perfil", this::crear),
                opcion("Modificar perfil", this::modificar),
                opcion("Activar/Desactivar perfil", this::cambiarEstado)
        );
    }

    private void listar() {
        List<Perfil> perfiles = dao.getPerfilDAO().obtenerTodos();
        System.out.println("\n=== PERFILES ===");
        for (Perfil p : perfiles) {
            String estado = p.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("  %d - %s [%s]%n", p.getId(), p.getNombre(), estado);
        }
    }

    private void verDetalle() {
        int id = pedirInt("ID del perfil: ");
        Perfil p = dao.getPerfilDAO().obtenerPorIdConFuncionalidades(id);
        if (p == null) {
            System.out.println("Perfil no encontrado.");
            return;
        }
        
        System.out.println("\n=== DETALLE DE PERFIL ===");
        System.out.println("ID: " + p.getId());
        System.out.println("Nombre: " + p.getNombre());
        System.out.println("Descripcion: " + p.getDescripcion());
        System.out.println("Estado: " + (p.isActivo() ? "ACTIVO" : "INACTIVO"));
        System.out.println("\nFuncionalidades asignadas:");
        if (p.getFuncionalidades() == null || p.getFuncionalidades().isEmpty()) {
            System.out.println("  (ninguna)");
        } else {
            for (Funcionalidad f : p.getFuncionalidades()) {
                System.out.printf("  - %s: %s%n", f.getNombre(), f.getDescripcion());
            }
        }
    }

    private void crear() {
        System.out.println("\n=== CREAR PERFIL ===");
        String nombre = pedirString("Nombre: ", s -> s.length() < 3 ? "minimo 3 caracteres" : null);
        String descripcion = pedirStringOpcional("Descripcion");
        
        Perfil p = new Perfil();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setActivo(true);
        
        dao.getPerfilDAO().insertar(p);
        System.out.println("Perfil creado con ID: " + p.getId());
        
        if (pedirBoolean("¿Desea asignar funcionalidades ahora? (si/no): ")) {
            asignarFuncionalidades(p.getId());
        }
    }

    private void modificar() {
        int id = pedirInt("ID del perfil a modificar: ");
        Perfil p = dao.getPerfilDAO().obtenerPorId(id);
        if (p == null) {
            System.out.println("Perfil no encontrado.");
            return;
        }
        
        System.out.println("Modificando: " + p.getNombre());
        
        String nombre = pedirString("Nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) p.setNombre(nombre);
        
        String descripcion = pedirString("Nueva descripcion (enter para mantener): ");
        if (!descripcion.isBlank()) p.setDescripcion(descripcion);
        
        dao.getPerfilDAO().actualizar(p);
        System.out.println("Perfil actualizado.");
        
        if (pedirBoolean("¿Desea modificar funcionalidades? (si/no): ")) {
            asignarFuncionalidades(p.getId());
        }
    }

    private void cambiarEstado() {
        int id = pedirInt("ID del perfil: ");
        Perfil p = dao.getPerfilDAO().obtenerPorId(id);
        if (p == null) {
            System.out.println("Perfil no encontrado.");
            return;
        }
        
        boolean nuevoEstado = !p.isActivo();
        p.setActivo(nuevoEstado);
        dao.getPerfilDAO().actualizar(p);
        System.out.println("Perfil " + (nuevoEstado ? "activado" : "desactivado") + ".");
    }

    private void asignarFuncionalidades(int idPerfil) {
        List<Funcionalidad> todas = dao.getFuncionalidadDAO().obtenerTodas();
        Perfil p = dao.getPerfilDAO().obtenerPorIdConFuncionalidades(idPerfil);
        
        System.out.println("\nFuncionalidades disponibles:");
        for (Funcionalidad f : todas) {
            boolean asignada = p.getFuncionalidades() != null && 
                p.getFuncionalidades().stream().anyMatch(x -> x.getId() == f.getId());
            System.out.printf("  %d - %s %s%n", f.getId(), f.getNombre(), asignada ? "[ASIGNADA]" : "");
        }
        
        System.out.println("\nIngrese IDs a agregar (0 para terminar):");
        while (true) {
            int idFunc = pedirInt("ID funcionalidad: ");
            if (idFunc == 0) break;
            dao.getPerfilDAO().agregarFuncionalidad(idPerfil, idFunc);
            System.out.println("Funcionalidad agregada.");
        }
        
        if (pedirBoolean("¿Desea quitar alguna funcionalidad? (si/no): ")) {
            System.out.println("Ingrese IDs a quitar (0 para terminar):");
            while (true) {
                int idFunc = pedirInt("ID funcionalidad: ");
                if (idFunc == 0) break;
                dao.getPerfilDAO().quitarFuncionalidad(idPerfil, idFunc);
                System.out.println("Funcionalidad quitada.");
            }
        }
    }
}
