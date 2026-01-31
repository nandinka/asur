package com.asur.daos.interfaces;

import com.asur.modelos.Reserva;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public interface ReservaDAO {
    Reserva crearDesdeResultSet(ResultSet rs) throws SQLException;
    Reserva obtenerPorId(int id);
    List<Reserva> obtenerTodos();
    List<Reserva> obtenerPorUsuario(int idUsuario);
    List<Reserva> obtenerPorRecurso(int idRecurso);
    List<Reserva> obtenerPorFecha(Date fecha);
    List<Reserva> filtrar(Date fechaDesde, Date fechaHasta, Boolean activa, int[] idsRecurso);
    boolean existe(int id);
    boolean hayConflicto(int idRecurso, Date fecha, Time hora, Time duracion);
    void insertar(Reserva reserva);
    void actualizar(Reserva reserva);
    void cancelar(int id);
}
