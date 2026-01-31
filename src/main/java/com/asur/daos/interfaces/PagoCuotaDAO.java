package com.asur.daos.interfaces;

import com.asur.modelos.PagoCuota;
import java.util.List;

public interface PagoCuotaDAO {
    void insertar(PagoCuota p);
    List<PagoCuota> obtenerPorCuota(int idCuota);
}
