package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.Recurso;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class RecursoConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public RecursoConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Espacios/Recursos",
                opcion("Listar espacios", this::listar),
                opcion("Ver detalle", this::verDetalle),
                opcion("Crear espacio", this::crear),
                opcion("Modificar espacio", this::modificar),
                opcion("Activar/Desactivar espacio", this::cambiarEstado)
        );
    }

    private void listar() {
        List<Recurso> recursos = dao.getRecursoDAO().obtenerTodos();
        System.out.println("\n=== ESPACIOS/SALONES ===");
        for (Recurso r : recursos) {
            String estado = r.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("  %d - %s (cap: %d) [%s]%n", r.getId(), r.getNombre(), r.getCapMax(), estado);
        }
    }

    private void verDetalle() {
        int id = pedirInt("ID del espacio: ");
        Recurso r = dao.getRecursoDAO().obtenerPorId(id);
        if (r == null) {
            System.out.println("Espacio no encontrado.");
            return;
        }
        
        System.out.println("\n=== DETALLE DE ESPACIO ===");
        System.out.println("ID: " + r.getId());
        System.out.println("Nombre: " + r.getNombre());
        System.out.println("Descripcion: " + r.getDescripcion());
        System.out.println("Capacidad maxima: " + r.getCapMax() + " personas");
        System.out.println("Precio/hora SOCIO: $" + r.getCostoHoraSocio());
        System.out.println("Precio/hora NO SOCIO: $" + r.getCostoHoraNoSocio());
        System.out.println("Vigencia precios: " + r.getFechaVigenciaPrecios());
        System.out.println("Estado: " + (r.isActivo() ? "ACTIVO" : "INACTIVO"));
    }

    private void crear() {
        System.out.println("\n=== CREAR ESPACIO ===");
        String nombre = pedirString("Nombre: ", s -> s.length() < 3 ? "minimo 3 caracteres" : null);
        String descripcion = pedirStringOpcional("Descripcion");
        int capMax = pedirInt("Capacidad maxima: ", c -> c <= 0 ? "debe ser mayor a 0" : null);
        BigDecimal precioSocio = pedirBigDecimal("Precio/hora SOCIO: $");
        BigDecimal precioNoSocio = pedirBigDecimal("Precio/hora NO SOCIO: $");
        
        Recurso r = new Recurso();
        r.setNombre(nombre);
        r.setDescripcion(descripcion);
        r.setCapMax(capMax);
        r.setCostoHoraSocio(precioSocio);
        r.setCostoHoraNoSocio(precioNoSocio);
        r.setFechaVigenciaPrecios(new Date(System.currentTimeMillis()));
        r.setActivo(true);
        
        dao.getRecursoDAO().insertar(r);
        System.out.println("Espacio creado con ID: " + r.getId());
    }

    private void modificar() {
        int id = pedirInt("ID del espacio a modificar: ");
        Recurso r = dao.getRecursoDAO().obtenerPorId(id);
        if (r == null) {
            System.out.println("Espacio no encontrado.");
            return;
        }
        
        System.out.println("Modificando: " + r.getNombre());
        
        String nombre = pedirString("Nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) r.setNombre(nombre);
        
        String descripcion = pedirString("Nueva descripcion (enter para mantener): ");
        if (!descripcion.isBlank()) r.setDescripcion(descripcion);
        
        String capStr = pedirString("Nueva capacidad (enter para mantener): ");
        if (!capStr.isBlank()) {
            try {
                r.setCapMax(Integer.parseInt(capStr));
            } catch (NumberFormatException e) {}
        }
        
        if (pedirBoolean("¿Actualizar precios? (si/no): ")) {
            r.setCostoHoraSocio(pedirBigDecimal("Nuevo precio SOCIO: $"));
            r.setCostoHoraNoSocio(pedirBigDecimal("Nuevo precio NO SOCIO: $"));
            r.setFechaVigenciaPrecios(new Date(System.currentTimeMillis()));
        }
        
        dao.getRecursoDAO().actualizar(r);
        System.out.println("Espacio actualizado.");
    }

    private void cambiarEstado() {
        int id = pedirInt("ID del espacio: ");
        Recurso r = dao.getRecursoDAO().obtenerPorId(id);
        if (r == null) {
            System.out.println("Espacio no encontrado.");
            return;
        }
        
        if (r.isActivo()) {
            dao.getRecursoDAO().darBaja(id);
            System.out.println("Espacio desactivado.");
        } else {
            r.setActivo(true);
            dao.getRecursoDAO().actualizar(r);
            System.out.println("Espacio activado.");
        }
    }
}
