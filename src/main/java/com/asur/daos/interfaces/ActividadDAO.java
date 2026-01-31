package com.asur.daos.interfaces;

import com.asur.modelos.Actividad;
import java.sql.Date;
import java.util.List;

public interface ActividadDAO {
    Actividad obtenerPorId(int id);
    List<Actividad> obtenerTodas();
    List<Actividad> obtenerActivas();
    List<Actividad> obtenerDisponibles();
    List<Actividad> filtrar(Date desde, Date hasta, Integer idTipo);
    void insertar(Actividad a);
    void actualizar(Actividad a);
    void cancelar(int id);
    void incrementarInscritos(int id);
    void decrementarInscritos(int id);
}
