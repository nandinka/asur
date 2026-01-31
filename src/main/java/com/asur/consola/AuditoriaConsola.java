package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.Auditoria;
import com.asur.modelos.Perfil;
import com.asur.modelos.Usuario;
import static com.asur.utils.Consola.*;
import java.sql.Date;
import java.util.List;

public class AuditoriaConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public AuditoriaConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Auditoria del Sistema",
                opcion("Ver ultimos registros", this::verUltimos),
                opcion("Buscar por usuario", this::buscarPorUsuario),
                opcion("Buscar por fecha", this::buscarPorFecha),
                opcion("Buscar por operacion", this::buscarPorOperacion),
                opcion("Reporte completo", this::reporteCompleto)
        );
    }

    private void verUltimos() {
        int cantidad = pedirInt("Cantidad de registros a mostrar: ", c -> c <= 0 || c > 100 ? "entre 1 y 100" : null);
        List<Auditoria> registros = dao.getAuditoriaDAO().obtenerUltimos(cantidad);
        mostrarRegistros(registros);
    }

    private void buscarPorUsuario() {
        int idUsuario = pedirInt("ID del usuario: ");
        Usuario u = dao.getUsuarioDAO().obtenerPorId(idUsuario);
        if (u == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        
        System.out.println("Buscando registros de: " + u.getNombreCompleto());
        List<Auditoria> registros = dao.getAuditoriaDAO().obtenerPorUsuario(idUsuario);
        mostrarRegistros(registros);
    }

    private void buscarPorFecha() {
        Date desde = pedirDate("Fecha desde");
        Date hasta = pedirDate("Fecha hasta");
        List<Auditoria> registros = dao.getAuditoriaDAO().obtenerPorRangoFecha(desde, hasta);
        mostrarRegistros(registros);
    }

    private void buscarPorOperacion() {
        System.out.println("Operaciones comunes: LOGIN, INSERT, UPDATE, DELETE");
        String operacion = pedirString("Operacion: ").toUpperCase();
        List<Auditoria> registros = dao.getAuditoriaDAO().obtenerPorOperacion(operacion);
        mostrarRegistros(registros);
    }

    private void reporteCompleto() {
        System.out.println("\n=== REPORTE DE AUDITORIA ===");
        Date desde = pedirDate("Fecha desde");
        Date hasta = pedirDate("Fecha hasta");
        
        List<Auditoria> registros = dao.getAuditoriaDAO().obtenerPorRangoFecha(desde, hasta);
        
        long logins = registros.stream().filter(a -> "LOGIN".equals(a.getOperacion())).count();
        long inserts = registros.stream().filter(a -> "INSERT".equals(a.getOperacion())).count();
        long updates = registros.stream().filter(a -> "UPDATE".equals(a.getOperacion())).count();
        long deletes = registros.stream().filter(a -> "DELETE".equals(a.getOperacion())).count();
        
        System.out.println("\nResumen del " + desde + " al " + hasta + ":");
        System.out.println("Total registros: " + registros.size());
        System.out.println("Logins: " + logins);
        System.out.println("Inserts: " + inserts);
        System.out.println("Updates: " + updates);
        System.out.println("Deletes: " + deletes);
        System.out.println("Otras operaciones: " + (registros.size() - logins - inserts - updates - deletes));
        
        if (pedirBoolean("¿Ver detalle? (si/no): ")) {
            mostrarRegistros(registros);
        }
    }

    private void mostrarRegistros(List<Auditoria> registros) {
        System.out.println("\n=== REGISTROS DE AUDITORIA ===");
        if (registros.isEmpty()) {
            System.out.println("No hay registros.");
            return;
        }
        
        for (Auditoria a : registros) {
            Usuario u = dao.getUsuarioDAO().obtenerPorId(a.getIdUsuario());
            System.out.printf("  %d | %s | %s | %s | %s | IP: %s%n", 
                a.getId(), 
                a.getFechaHora(),
                u != null ? u.getNombreCompleto() : "Usuario #" + a.getIdUsuario(),
                a.getOperacion(),
                a.getDetalle() != null ? a.getDetalle() : "",
                a.getIpCliente() != null ? a.getIpCliente() : "N/A");
        }
        System.out.println("\nTotal: " + registros.size() + " registros");
    }
}
