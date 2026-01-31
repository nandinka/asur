package com.asur.consola;

import com.asur.daos.factory.DAOFactory;
import com.asur.modelos.*;
import static com.asur.utils.Consola.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class PagoConsola {
    private final DAOFactory dao;
    private final Usuario sesion;
    private final Perfil perfil;

    public PagoConsola(DAOFactory dao, Usuario sesion, Perfil perfil) {
        this.dao = dao;
        this.sesion = sesion;
        this.perfil = perfil;
    }

    public void mostrarMenu() {
        pedirSeleccion("Gestion de Pagos",
                opcion("Registrar pago de reserva", this::pagoReserva),
                opcion("Registrar pago de actividad", this::pagoActividad),
                opcion("Registrar pago de cuota", this::pagoCuota),
                opcion("Ver pagos de reserva", this::verPagosReserva),
                opcion("Ver pagos de actividad", this::verPagosActividad)
        );
    }

    private void pagoReserva() {
        System.out.println("\n=== PAGO DE RESERVA ===");
        
        int idReserva = pedirInt("ID de la reserva: ");
        Reserva r = dao.getReservaDAO().obtenerPorId(idReserva);
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        
        if (!r.isActiva()) {
            System.out.println("La reserva esta cancelada.");
            return;
        }
        
        Recurso rec = dao.getRecursoDAO().obtenerPorId(r.getIdRecurso());
        Usuario u = dao.getUsuarioDAO().obtenerPorId(r.getIdUsuario());
        
        System.out.println("\nReserva #" + r.getId());
        System.out.println("Espacio: " + (rec != null ? rec.getNombre() : "?"));
        System.out.println("Usuario: " + (u != null ? u.getNombreCompleto() : "?"));
        System.out.println("Fecha: " + r.getFecha());
        System.out.println("Monto total: $" + r.getMontoTotal());
        System.out.println("Seña requerida: $" + r.getImporteSena());
        System.out.println("Seña pagada: $" + r.getImporteSenaPagado());
        System.out.println("Saldo pendiente: $" + r.getSaldoPendiente());
        
        if (r.getSaldoPendiente().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("\nLa reserva ya esta pagada.");
            return;
        }
        
        // tipo de pago
        System.out.println("\nTipo de pago:");
        System.out.println("  1. Seña");
        System.out.println("  2. Pago total");
        System.out.println("  3. Pago parcial");
        int tipoPago = pedirInt("Seleccione: ", t -> t < 1 || t > 3 ? "opcion invalida" : null);
        
        BigDecimal monto;
        if (tipoPago == 1) {
            monto = r.getImporteSena().subtract(r.getImporteSenaPagado() != null ? r.getImporteSenaPagado() : BigDecimal.ZERO);
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("La seña ya fue pagada.");
                return;
            }
        } else if (tipoPago == 2) {
            monto = r.getSaldoPendiente();
        } else {
            monto = pedirBigDecimal("Monto a pagar: $");
            if (monto.compareTo(r.getSaldoPendiente()) > 0) {
                System.out.println("El monto excede el saldo pendiente.");
                return;
            }
        }
        
        // forma de pago
        System.out.println("\nForma de pago:");
        System.out.println("  1. Efectivo");
        System.out.println("  2. Transferencia");
        System.out.println("  3. Tarjeta Debito");
        System.out.println("  4. Tarjeta Credito");
        System.out.println("  5. MercadoPago");
        int formaPago = pedirInt("Seleccione: ", f -> f < 1 || f > 5 ? "opcion invalida" : null);
        
        System.out.println("\nMonto a pagar: $" + monto);
        if (!pedirBoolean("¿Confirmar pago? (si/no): ")) {
            return;
        }
        
        // registrar pago
        PagoReserva pago = new PagoReserva();
        pago.setIdReserva(idReserva);
        pago.setMonto(monto);
        pago.setFechaPago(new Timestamp(System.currentTimeMillis()));
        pago.setIdFormaPago(formaPago);
        
        dao.getPagoReservaDAO().insertar(pago);
        
        // actualizar seña pagada si corresponde
        if (tipoPago == 1 || tipoPago == 3) {
            BigDecimal nuevaSena = (r.getImporteSenaPagado() != null ? r.getImporteSenaPagado() : BigDecimal.ZERO).add(monto);
            if (nuevaSena.compareTo(r.getImporteSena()) > 0) {
                nuevaSena = r.getImporteSena();
            }
            r.setImporteSenaPagado(nuevaSena);
            if (tipoPago == 1) {
                r.setFechaPagoSena(new Date(System.currentTimeMillis()));
            }
        } else {
            r.setImporteSenaPagado(r.getImporteSena());
            r.setFechaPagoSena(new Date(System.currentTimeMillis()));
        }
        
        // actualizar estado de pago
        BigDecimal totalPagado = (r.getImporteSenaPagado() != null ? r.getImporteSenaPagado() : BigDecimal.ZERO);
        if (totalPagado.compareTo(r.getMontoTotal()) >= 0) {
            r.setIdEstadoPagoReserva(1); // Pagado
        } else if (totalPagado.compareTo(BigDecimal.ZERO) > 0) {
            r.setIdEstadoPagoReserva(2); // Parcial
        }
        
        dao.getReservaDAO().actualizar(r);
        System.out.println("¡Pago registrado exitosamente!");
    }

    private void pagoActividad() {
        System.out.println("\n=== PAGO DE ACTIVIDAD ===");
        
        // mostrar inscripciones del usuario
        List<InscripcionActividad> inscripciones = dao.getInscripcionDAO().obtenerPorUsuario(sesion.getId());
        inscripciones = inscripciones.stream().filter(InscripcionActividad::isActiva).toList();
        
        if (inscripciones.isEmpty()) {
            System.out.println("No tienes inscripciones activas.");
            return;
        }
        
        System.out.println("Tus inscripciones:");
        for (InscripcionActividad i : inscripciones) {
            Actividad a = dao.getActividadDAO().obtenerPorId(i.getIdActividad());
            boolean esSocio = sesion.getIdPerfil() == 2;
            BigDecimal costo = esSocio ? a.getCostoSocio() : a.getCostoNoSocio();
            System.out.printf("  %d - %s | %s | $%s%n", i.getId(), a.getNombre(), a.getFecha(), costo);
        }
        
        int idInscripcion = pedirInt("ID de la inscripcion: ");
        InscripcionActividad ins = dao.getInscripcionDAO().obtenerPorId(idInscripcion);
        
        if (ins == null || ins.getIdUsuario() != sesion.getId()) {
            System.out.println("Inscripcion no encontrada.");
            return;
        }
        
        Actividad a = dao.getActividadDAO().obtenerPorId(ins.getIdActividad());
        boolean esSocio = sesion.getIdPerfil() == 2;
        BigDecimal monto = esSocio ? a.getCostoSocio() : a.getCostoNoSocio();
        
        System.out.println("\nActividad: " + a.getNombre());
        System.out.println("Monto: $" + monto);
        
        // forma de pago
        System.out.println("\nForma de pago:");
        System.out.println("  1. Efectivo");
        System.out.println("  2. Transferencia");
        System.out.println("  3. Tarjeta Debito");
        System.out.println("  4. Tarjeta Credito");
        System.out.println("  5. MercadoPago");
        int formaPago = pedirInt("Seleccione: ", f -> f < 1 || f > 5 ? "opcion invalida" : null);
        
        if (!pedirBoolean("¿Confirmar pago? (si/no): ")) {
            return;
        }
        
        PagoActividad pago = new PagoActividad();
        pago.setIdInscripcion(idInscripcion);
        pago.setMonto(monto);
        pago.setFechaPago(new Timestamp(System.currentTimeMillis()));
        pago.setIdFormaPago(formaPago);
        pago.setIdEstadoPago(1); // Pagado
        
        dao.getPagoActividadDAO().insertar(pago);
        System.out.println("¡Pago registrado exitosamente!");
    }

    private void pagoCuota() {
        System.out.println("\n=== PAGO DE CUOTA ===");
        
        // solo para socios
        if (sesion.getIdPerfil() != 2) {
            System.out.println("Solo los socios pueden pagar cuotas.");
            return;
        }
        
        // obtener cuotas pendientes
        List<Cuota> pendientes = dao.getCuotaDAO().obtenerPendientesPorUsuario(sesion.getId());
        
        if (pendientes.isEmpty()) {
            System.out.println("No tienes cuotas pendientes.");
            return;
        }
        
        System.out.println("Cuotas pendientes:");
        for (Cuota c : pendientes) {
            System.out.printf("  %d - %s | $%s%n", c.getId(), c.getPeriodo(), c.getMonto());
        }
        
        int idCuota = pedirInt("ID de la cuota: ");
        Cuota cuota = dao.getCuotaDAO().obtenerPorId(idCuota);
        
        if (cuota == null || cuota.getIdUsuario() != sesion.getId()) {
            System.out.println("Cuota no encontrada.");
            return;
        }
        
        System.out.println("\nCuota: " + cuota.getPeriodo());
        System.out.println("Monto: $" + cuota.getMonto());
        
        // forma de pago
        System.out.println("\nForma de pago:");
        System.out.println("  1. Efectivo");
        System.out.println("  2. Transferencia");
        System.out.println("  3. Tarjeta Debito");
        System.out.println("  4. Tarjeta Credito");
        System.out.println("  5. MercadoPago");
        int formaPago = pedirInt("Seleccione: ", f -> f < 1 || f > 5 ? "opcion invalida" : null);
        
        if (!pedirBoolean("¿Confirmar pago? (si/no): ")) {
            return;
        }
        
        PagoCuota pago = new PagoCuota();
        pago.setIdCuota(idCuota);
        pago.setMonto(cuota.getMonto());
        pago.setFechaPago(new Timestamp(System.currentTimeMillis()));
        pago.setIdFormaPago(formaPago);
        
        dao.getPagoCuotaDAO().insertar(pago);
        dao.getCuotaDAO().marcarPagada(idCuota);
        
        System.out.println("¡Pago registrado exitosamente!");
    }

    private void verPagosReserva() {
        int idReserva = pedirInt("ID de la reserva: ");
        List<PagoReserva> pagos = dao.getPagoReservaDAO().obtenerPorReserva(idReserva);
        
        System.out.println("\n=== PAGOS DE RESERVA #" + idReserva + " ===");
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados.");
            return;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        for (PagoReserva p : pagos) {
            System.out.printf("  %d | %s | $%s%n", p.getId(), p.getFechaPago(), p.getMonto());
            total = total.add(p.getMonto());
        }
        System.out.println("Total pagado: $" + total);
    }

    private void verPagosActividad() {
        int idInscripcion = pedirInt("ID de la inscripcion: ");
        List<PagoActividad> pagos = dao.getPagoActividadDAO().obtenerPorInscripcion(idInscripcion);
        
        System.out.println("\n=== PAGOS DE INSCRIPCION #" + idInscripcion + " ===");
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados.");
            return;
        }
        
        for (PagoActividad p : pagos) {
            System.out.printf("  %d | %s | $%s%n", p.getId(), p.getFechaPago(), p.getMonto());
        }
    }
}
