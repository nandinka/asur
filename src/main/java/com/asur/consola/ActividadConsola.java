package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.*;
import static com.asur.utils.Consola.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ActividadConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public ActividadConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Actividades",
                opcion("Listar actividades", this::listar),
                opcion("Ver detalle", this::verDetalle),
                opcion("Crear actividad", this::crear),
                opcion("Modificar actividad", this::modificar),
                opcion("Cancelar actividad", this::cancelar),
                opcion("Inscribirse a actividad", this::inscribirse),
                opcion("Mis inscripciones", this::misInscripciones),
                opcion("Cancelar inscripcion", this::cancelarInscripcion),
                opcion("Reporte por tipo y fechas", this::reportePorTipo)
        );
    }

    private void listar() {
        List<Actividad> actividades = dao.getActividadDAO().obtenerTodas();
        System.out.println("\n=== ACTIVIDADES ===");
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades.");
            return;
        }
        for (Actividad a : actividades) {
            TipoActividad tipo = dao.getTipoActividadDAO().obtenerPorId(a.getIdTipoActividad());
            String estado = a.isActiva() ? "ACTIVA" : "CANCELADA";
            System.out.printf("  %d - %s | %s | %s %s | %d/%d inscritos [%s]%n", 
                a.getId(), a.getNombre(), 
                tipo != null ? tipo.getNombre() : "?",
                a.getFecha(), a.getHora(),
                a.getInscritos(), a.getCupoMax(), estado);
        }
    }

    private void verDetalle() {
        int id = pedirInt("ID de la actividad: ");
        Actividad a = dao.getActividadDAO().obtenerPorId(id);
        if (a == null) {
            System.out.println("Actividad no encontrada.");
            return;
        }
        
        TipoActividad tipo = dao.getTipoActividadDAO().obtenerPorId(a.getIdTipoActividad());
        Recurso rec = a.getIdRecurso() != null ? dao.getRecursoDAO().obtenerPorId(a.getIdRecurso()) : null;
        
        System.out.println("\n=== DETALLE ACTIVIDAD #" + a.getId() + " ===");
        System.out.println("Nombre: " + a.getNombre());
        System.out.println("Descripcion: " + a.getDescripcion());
        System.out.println("Tipo: " + (tipo != null ? tipo.getNombre() : "?"));
        System.out.println("Fecha: " + a.getFecha());
        System.out.println("Hora: " + a.getHora());
        System.out.println("Duracion: " + a.getDuracion());
        System.out.println("Lugar: " + (rec != null ? rec.getNombre() : a.getLugar()));
        System.out.println("Cupo: " + a.getInscritos() + "/" + a.getCupoMax());
        System.out.println("Costo SOCIO: $" + a.getCostoSocio());
        System.out.println("Costo NO SOCIO: $" + a.getCostoNoSocio());
        System.out.println("Estado: " + (a.isActiva() ? "ACTIVA" : "CANCELADA"));
        
        // mostrar inscritos
        List<InscripcionActividad> inscritos = dao.getInscripcionDAO().obtenerPorActividad(id);
        if (!inscritos.isEmpty()) {
            System.out.println("\nInscritos:");
            for (InscripcionActividad i : inscritos) {
                Usuario u = dao.getUsuarioDAO().obtenerPorId(i.getIdUsuario());
                String estadoIns = i.isActiva() ? "" : "[CANCELADA]";
                System.out.printf("  - %s %s%n", u != null ? u.getNombreCompleto() : "?", estadoIns);
            }
        }
    }

    private void crear() {
        System.out.println("\n=== CREAR ACTIVIDAD ===");
        
        String nombre = pedirString("Nombre: ", s -> s.length() < 3 ? "minimo 3 caracteres" : null);
        String descripcion = pedirStringOpcional("Descripcion");
        
        // tipo de actividad
        List<TipoActividad> tipos = dao.getTipoActividadDAO().obtenerActivos();
        System.out.println("Tipos de actividad:");
        for (TipoActividad t : tipos) {
            System.out.printf("  %d - %s%n", t.getId(), t.getNombre());
        }
        int idTipo = pedirInt("ID del tipo: ");
        
        Date fecha = pedirDateFutura("Fecha");
        Time hora = pedirTime("Hora (HH:MM): ");
        int horas = pedirInt("Duracion en horas: ", h -> h <= 0 ? "debe ser mayor a 0" : null);
        Time duracion = Time.valueOf(String.format("%02d:00:00", horas));
        
        int cupoMax = pedirInt("Cupo maximo: ", c -> c <= 0 ? "debe ser mayor a 0" : null);
        
        BigDecimal costoSocio = pedirBigDecimal("Costo SOCIO: $");
        BigDecimal costoNoSocio = pedirBigDecimal("Costo NO SOCIO: $");
        
        // lugar
        String lugar = null;
        Integer idRecurso = null;
        if (pedirBoolean("¿Usar espacio de ASUR? (si/no): ")) {
            List<Recurso> recursos = dao.getRecursoDAO().obtenerActivos();
            System.out.println("Espacios:");
            for (Recurso r : recursos) {
                System.out.printf("  %d - %s (cap: %d)%n", r.getId(), r.getNombre(), r.getCapMax());
            }
            idRecurso = pedirInt("ID del espacio: ");
        } else {
            lugar = pedirString("Lugar externo: ");
        }
        
        Actividad a = new Actividad();
        a.setNombre(nombre);
        a.setDescripcion(descripcion);
        a.setIdTipoActividad(idTipo);
        a.setFecha(fecha);
        a.setHora(hora);
        a.setDuracion(duracion);
        a.setCupoMax(cupoMax);
        a.setInscritos(0);
        a.setCostoSocio(costoSocio);
        a.setCostoNoSocio(costoNoSocio);
        a.setIdRecurso(idRecurso);
        a.setLugar(lugar);
        a.setActiva(true);
        
        dao.getActividadDAO().insertar(a);
        System.out.println("Actividad creada con ID: " + a.getId());
    }

    private void modificar() {
        int id = pedirInt("ID de la actividad: ");
        Actividad a = dao.getActividadDAO().obtenerPorId(id);
        if (a == null) {
            System.out.println("Actividad no encontrada.");
            return;
        }
        
        System.out.println("Modificando: " + a.getNombre());
        
        String nombre = pedirString("Nuevo nombre (enter para mantener): ");
        if (!nombre.isBlank()) a.setNombre(nombre);
        
        String descripcion = pedirString("Nueva descripcion (enter para mantener): ");
        if (!descripcion.isBlank()) a.setDescripcion(descripcion);
        
        String cupoStr = pedirString("Nuevo cupo (enter para mantener): ");
        if (!cupoStr.isBlank()) {
            try {
                a.setCupoMax(Integer.parseInt(cupoStr));
            } catch (NumberFormatException e) {}
        }
        
        if (pedirBoolean("¿Actualizar costos? (si/no): ")) {
            a.setCostoSocio(pedirBigDecimal("Nuevo costo SOCIO: $"));
            a.setCostoNoSocio(pedirBigDecimal("Nuevo costo NO SOCIO: $"));
        }
        
        dao.getActividadDAO().actualizar(a);
        System.out.println("Actividad actualizada.");
    }

    private void cancelar() {
        int id = pedirInt("ID de la actividad a cancelar: ");
        Actividad a = dao.getActividadDAO().obtenerPorId(id);
        if (a == null) {
            System.out.println("Actividad no encontrada.");
            return;
        }
        
        if (!a.isActiva()) {
            System.out.println("La actividad ya esta cancelada.");
            return;
        }
        
        if (pedirBoolean("¿Confirmar cancelacion? (si/no): ")) {
            dao.getActividadDAO().cancelar(id);
            System.out.println("Actividad cancelada.");
        }
    }

    private void inscribirse() {
        System.out.println("\n=== INSCRIPCION A ACTIVIDAD ===");
        
        // mostrar actividades disponibles
        List<Actividad> disponibles = dao.getActividadDAO().obtenerDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay actividades disponibles.");
            return;
        }
        
        System.out.println("Actividades disponibles:");
        for (Actividad a : disponibles) {
            System.out.printf("  %d - %s | %s | %d/%d | socio: $%s, no socio: $%s%n", 
                a.getId(), a.getNombre(), a.getFecha(), 
                a.getInscritos(), a.getCupoMax(),
                a.getCostoSocio(), a.getCostoNoSocio());
        }
        
        int idActividad = pedirInt("ID de la actividad: ");
        Actividad a = dao.getActividadDAO().obtenerPorId(idActividad);
        
        if (a == null || !a.isActiva()) {
            System.out.println("Actividad no disponible.");
            return;
        }
        
        if (a.getInscritos() >= a.getCupoMax()) {
            System.out.println("La actividad esta llena.");
            return;
        }
        
        // verificar si ya esta inscrito
        if (dao.getInscripcionDAO().estaInscrito(sesion.getId(), idActividad)) {
            System.out.println("Ya estas inscrito en esta actividad.");
            return;
        }
        
        boolean esSocio = sesion.getIdPerfil() == 2;
        BigDecimal costo = esSocio ? a.getCostoSocio() : a.getCostoNoSocio();
        
        System.out.println("\nActividad: " + a.getNombre());
        System.out.println("Fecha: " + a.getFecha() + " " + a.getHora());
        System.out.println("Costo: $" + costo + " (" + (esSocio ? "SOCIO" : "NO SOCIO") + ")");
        
        if (!pedirBoolean("¿Confirmar inscripcion? (si/no): ")) {
            return;
        }
        
        InscripcionActividad ins = new InscripcionActividad();
        ins.setIdUsuario(sesion.getId());
        ins.setIdActividad(idActividad);
        ins.setFechaInscripcion(new Date(System.currentTimeMillis()));
        ins.setActiva(true);
        
        dao.getInscripcionDAO().insertar(ins);
        dao.getActividadDAO().incrementarInscritos(idActividad);
        
        System.out.println("¡Inscripcion exitosa!");
    }

    private void misInscripciones() {
        List<InscripcionActividad> inscripciones = dao.getInscripcionDAO().obtenerPorUsuario(sesion.getId());
        System.out.println("\n=== MIS INSCRIPCIONES ===");
        if (inscripciones.isEmpty()) {
            System.out.println("No tienes inscripciones.");
            return;
        }
        for (InscripcionActividad i : inscripciones) {
            Actividad a = dao.getActividadDAO().obtenerPorId(i.getIdActividad());
            String estado = i.isActiva() ? "ACTIVA" : "CANCELADA";
            System.out.printf("  %d - %s | %s [%s]%n", 
                i.getId(), a != null ? a.getNombre() : "?", 
                a != null ? a.getFecha().toString() : "?", estado);
        }
    }

    private void cancelarInscripcion() {
        List<InscripcionActividad> inscripciones = dao.getInscripcionDAO().obtenerPorUsuario(sesion.getId());
        inscripciones = inscripciones.stream().filter(InscripcionActividad::isActiva).toList();
        
        if (inscripciones.isEmpty()) {
            System.out.println("No tienes inscripciones activas.");
            return;
        }
        
        System.out.println("Tus inscripciones activas:");
        for (InscripcionActividad i : inscripciones) {
            Actividad a = dao.getActividadDAO().obtenerPorId(i.getIdActividad());
            System.out.printf("  %d - %s | %s%n", i.getId(), a != null ? a.getNombre() : "?", 
                a != null ? a.getFecha().toString() : "?");
        }
        
        int idInscripcion = pedirInt("ID de la inscripcion a cancelar: ");
        InscripcionActividad ins = dao.getInscripcionDAO().obtenerPorId(idInscripcion);
        
        if (ins == null || ins.getIdUsuario() != sesion.getId()) {
            System.out.println("Inscripcion no encontrada.");
            return;
        }
        
        if (!ins.isActiva()) {
            System.out.println("La inscripcion ya esta cancelada.");
            return;
        }
        
        if (pedirBoolean("¿Confirmar cancelacion? (si/no): ")) {
            dao.getInscripcionDAO().cancelar(idInscripcion);
            dao.getActividadDAO().decrementarInscritos(ins.getIdActividad());
            System.out.println("Inscripcion cancelada.");
        }
    }

    private void reportePorTipo() {
        System.out.println("\n=== REPORTE DE ACTIVIDADES ===");
        
        List<TipoActividad> tipos = dao.getTipoActividadDAO().obtenerTodos();
        System.out.println("Tipos disponibles (0 para todos):");
        for (TipoActividad t : tipos) {
            System.out.printf("  %d - %s%n", t.getId(), t.getNombre());
        }
        int idTipo = pedirInt("ID del tipo: ");
        
        Date desde = pedirDate("Fecha desde");
        Date hasta = pedirDate("Fecha hasta");
        
        List<Actividad> actividades = dao.getActividadDAO().filtrar(desde, hasta, idTipo > 0 ? idTipo : null);
        
        int total = actividades.size();
        int activas = (int) actividades.stream().filter(Actividad::isActiva).count();
        int totalInscritos = actividades.stream().mapToInt(Actividad::getInscritos).sum();
        
        System.out.println("\nResultados:");
        System.out.println("Total actividades: " + total);
        System.out.println("Activas: " + activas);
        System.out.println("Canceladas: " + (total - activas));
        System.out.println("Total inscritos: " + totalInscritos);
        
        if (pedirBoolean("¿Ver detalle? (si/no): ")) {
            for (Actividad a : actividades) {
                TipoActividad tipo = dao.getTipoActividadDAO().obtenerPorId(a.getIdTipoActividad());
                String estado = a.isActiva() ? "ACTIVA" : "CANCELADA";
                System.out.printf("  %d - %s | %s | %s | %d inscritos [%s]%n", 
                    a.getId(), a.getNombre(), tipo != null ? tipo.getNombre() : "?",
                    a.getFecha(), a.getInscritos(), estado);
            }
        }
    }
}
