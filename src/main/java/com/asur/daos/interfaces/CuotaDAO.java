package com.asur.daos.interfaces;

import com.asur.modelos.Cuota;
import java.util.List;

public interface CuotaDAO {
    Cuota obtenerPorId(int id);
    List<Cuota> obtenerPendientesPorUsuario(int idUsuario);
    void marcarPagada(int id);
}
