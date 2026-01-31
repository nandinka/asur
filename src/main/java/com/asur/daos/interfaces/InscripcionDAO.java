package com.asur.daos.interfaces;

import com.asur.modelos.InscripcionActividad;
import java.util.List;

public interface InscripcionDAO {
    InscripcionActividad obtenerPorId(int id);
    List<InscripcionActividad> obtenerPorUsuario(int idUsuario);
    List<InscripcionActividad> obtenerPorActividad(int idActividad);
    boolean estaInscrito(int idUsuario, int idActividad);
    void insertar(InscripcionActividad i);
    void cancelar(int id);
}
