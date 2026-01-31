package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Perfil;
import com.asur.modelos.Recurso;
import com.asur.modelos.Reserva;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class ReservaConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public ReservaConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Reservas",
                opcion("Listar reservas", this::listar),
                opcion("Mis reservas", this::misReservas),
                opcion("Nueva reserva", this::crear),
                opcion("Ver detalle", this::verDetalle),
                opcion("Cancelar reserva", this::cancelar),
                opcion("Reporte por fechas", this::reportePorFechas)
        );
    }

    private void listar() {
        List<Reserva> reservas = dao.getReservaDAO().obtenerTodos();
        System.out.println("\n=== RESERVAS ===");
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas.");
            return;
        }
        for (Reserva r : reservas) {
            Recurso rec = dao.getRecursoDAO().obtenerPorId(r.getIdRecurso());
            Usuario u = dao.getUsuarioDAO().obtenerPorId(r.getIdUsuario());
            String estado = r.isActiva() ? "ACTIVA" : "CANCELADA";
            System.out.printf("  %d - %s | %s %s | %s | %s [%s]%n", 
                r.getId(), rec != null ? rec.getNombre() : "?", 
                r.getFecha(), r.getHora(),
                u != null ? u.getNombreCompleto() : "?",
                "$" + r.getMontoTotal(), estado);
        }
    }

    private void misReservas() {
        List<Reserva> reservas = dao.getReservaDAO().obtenerPorUsuario(sesion.getId());
        System.out.println("\n=== MIS RESERVAS ===");
        if (reservas.isEmpty()) {
            System.out.println("No tienes reservas.");
            return;
        }
        for (Reserva r : reservas) {
            Recurso rec = dao.getRecursoDAO().obtenerPorId(r.getIdRecurso());
            String estado = r.isActiva() ? "ACTIVA" : "CANCELADA";
            System.out.printf("  %d - %s | %s %s | $%s [%s]%n", 
                r.getId(), rec != null ? rec.getNombre() : "?", 
                r.getFecha(), r.getHora(), r.getMontoTotal(), estado);
        }
    }

    private void crear() {
        System.out.println("\n=== NUEVA RESERVA ===");
        
        // mostrar espacios disponibles
        List<Recurso> recursos = dao.getRecursoDAO().obtenerActivos();
        System.out.println("Espacios disponibles:");
        for (Recurso r : recursos) {
            System.out.printf("  %d - %s (cap: %d, socio: $%s, no socio: $%s)%n", 
                r.getId(), r.getNombre(), r.getCapMax(), r.getCostoHoraSocio(), r.getCostoHoraNoSocio());
        }
        
        int idRecurso = pedirInt("ID del espacio: ");
        Recurso recurso = dao.getRecursoDAO().obtenerPorId(idRecurso);
        if (recurso == null || !recurso.isActivo()) {
            System.out.println("Espacio no disponible.");
            return;
        }
        
        Date fecha = pedirDateFutura("Fecha de la reserva");
        Time hora = pedirTime("Hora de inicio (HH:MM): ");
        int horas = pedirInt("Duracion en horas: ", h -> h <= 0 || h > 12 ? "entre 1 y 12 horas" : null);
        Time duracion = Time.valueOf(String.format("%02d:00:00", horas));
        
        int cantPersonas = pedirInt("Cantidad de personas: ", c -> c <= 0 ? "debe ser mayor a 0" : null);
        if (cantPersonas > recurso.getCapMax()) {
            System.out.println("Excede la capacidad maxima (" + recurso.getCapMax() + ")");
            return;
        }
        
        // verificar conflictos
        if (dao.getReservaDAO().hayConflicto(idRecurso, fecha, hora, duracion)) {
            System.out.println("El espacio ya esta reservado en ese horario.");
            return;
        }
        
        // calcular precio segun si es socio o no
        boolean esSocio = sesion.getIdPerfil() == 2; // perfil Socio
        BigDecimal precioHora = esSocio ? recurso.getCostoHoraSocio() : recurso.getCostoHoraNoSocio();
        BigDecimal montoTotal = precioHora.multiply(BigDecimal.valueOf(horas));
        
        // calcular seña (30% del total)
        BigDecimal importeSena = montoTotal.multiply(new BigDecimal("0.30"));
        
        // fecha vencimiento seña: 5 dias habiles antes de la reserva
        Date fechaVtoSena = calcularFechaVtoSena(fecha);
        
        String datosContacto = pedirString("Datos de contacto: ");
        
        System.out.println("\n=== RESUMEN ===");
        System.out.println("Espacio: " + recurso.getNombre());
        System.out.println("Fecha: " + fecha + " " + hora);
        System.out.println("Duracion: " + horas + " hora(s)");
        System.out.println("Personas: " + cantPersonas);
        System.out.println("Tipo: " + (esSocio ? "SOCIO" : "NO SOCIO"));
        System.out.println("Monto total: $" + montoTotal);
        System.out.println("Seña requerida: $" + importeSena);
        System.out.println("Vencimiento seña: " + fechaVtoSena);
        
        if (!pedirBoolean("¿Confirmar reserva? (si/no): ")) {
            System.out.println("Reserva cancelada.");
            return;
        }
        
        Reserva r = new Reserva();
        r.setIdRecurso(idRecurso);
        r.setIdUsuario(sesion.getId());
        r.setFecha(fecha);
        r.setHora(hora);
        r.setDuracion(duracion);
        r.setCantPersonas(cantPersonas);
        r.setMontoTotal(montoTotal);
        r.setImporteSena(importeSena);
        r.setFechaVtoSena(fechaVtoSena);
        r.setImporteSenaPagado(BigDecimal.ZERO);
        r.setDatosContacto(datosContacto);
        r.setIdEstadoPagoReserva(3); // Pendiente
        r.setActiva(true);
        
        dao.getReservaDAO().insertar(r);
        System.out.println("\n¡Reserva creada con ID: " + r.getId() + "!");
        System.out.println("Recuerde pagar la seña antes de: " + fechaVtoSena);
    }

    private void verDetalle() {
        int id = pedirInt("ID de la reserva: ");
        Reserva r = dao.getReservaDAO().obtenerPorId(id);
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        
        Recurso rec = dao.getRecursoDAO().obtenerPorId(r.getIdRecurso());
        Usuario u = dao.getUsuarioDAO().obtenerPorId(r.getIdUsuario());
        
        System.out.println("\n=== DETALLE RESERVA #" + r.getId() + " ===");
        System.out.println("Espacio: " + (rec != null ? rec.getNombre() : "?"));
        System.out.println("Usuario: " + (u != null ? u.getNombreCompleto() : "?"));
        System.out.println("Fecha: " + r.getFecha());
        System.out.println("Hora: " + r.getHora());
        System.out.println("Duracion: " + r.getDuracion());
        System.out.println("Personas: " + r.getCantPersonas());
        System.out.println("Contacto: " + r.getDatosContacto());
        System.out.println("Monto total: $" + r.getMontoTotal());
        System.out.println("Seña requerida: $" + r.getImporteSena());
        System.out.println("Seña pagada: $" + r.getImporteSenaPagado());
        System.out.println("Vto seña: " + r.getFechaVtoSena());
        System.out.println("Saldo pendiente: $" + r.getSaldoPendiente());
        System.out.println("Estado: " + (r.isActiva() ? "ACTIVA" : "CANCELADA"));
    }

    private void cancelar() {
        int id = pedirInt("ID de la reserva a cancelar: ");
        Reserva r = dao.getReservaDAO().obtenerPorId(id);
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        
        if (!r.isActiva()) {
            System.out.println("La reserva ya esta cancelada.");
            return;
        }
        
        // verificar permisos
        boolean esPropia = r.getIdUsuario() == sesion.getId();
        boolean esAdmin = sesion.getIdPerfil() == 1 || sesion.getIdPerfil() == 3;
        
        if (!esPropia && !esAdmin) {
            System.out.println("No tienes permiso para cancelar esta reserva.");
            return;
        }
        
        if (pedirBoolean("¿Confirmar cancelacion? (si/no): ")) {
            dao.getReservaDAO().cancelar(id);
            System.out.println("Reserva cancelada.");
        }
    }

    private void reportePorFechas() {
        System.out.println("\n=== REPORTE DE RESERVAS ===");
        Date desde = pedirDate("Fecha desde");
        Date hasta = pedirDate("Fecha hasta");
        
        List<Reserva> reservas = dao.getReservaDAO().filtrar(desde, hasta, null, null);
        
        int activas = 0, canceladas = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        
        for (Reserva r : reservas) {
            if (r.isActiva()) {
                activas++;
                totalMonto = totalMonto.add(r.getMontoTotal());
            } else {
                canceladas++;
            }
        }
        
        System.out.println("\nResultados del " + desde + " al " + hasta + ":");
        System.out.println("Total reservas: " + reservas.size());
        System.out.println("Activas: " + activas);
        System.out.println("Canceladas: " + canceladas);
        System.out.println("Monto total (activas): $" + totalMonto);
        
        if (pedirBoolean("¿Ver detalle? (si/no): ")) {
            for (Reserva r : reservas) {
                Recurso rec = dao.getRecursoDAO().obtenerPorId(r.getIdRecurso());
                String estado = r.isActiva() ? "ACTIVA" : "CANCELADA";
                System.out.printf("  %d - %s | %s | $%s [%s]%n", 
                    r.getId(), rec != null ? rec.getNombre() : "?", r.getFecha(), r.getMontoTotal(), estado);
            }
        }
    }

    private Date calcularFechaVtoSena(Date fechaReserva) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReserva);
        int diasHabiles = 5;
        while (diasHabiles > 0) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            if (diaSemana != Calendar.SATURDAY && diaSemana != Calendar.SUNDAY) {
                diasHabiles--;
            }
        }
        return new Date(cal.getTimeInMillis());
    }
}
