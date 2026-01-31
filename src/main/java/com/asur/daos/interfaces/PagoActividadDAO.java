package com.asur.daos.interfaces;

import com.asur.modelos.PagoActividad;
import java.util.List;

public interface PagoActividadDAO {
    void insertar(PagoActividad p);
    List<PagoActividad> obtenerPorInscripcion(int idInscripcion);
}
