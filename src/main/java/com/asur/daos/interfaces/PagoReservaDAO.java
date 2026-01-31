package com.asur.daos.interfaces;

import com.asur.modelos.PagoReserva;
import java.util.List;

public interface PagoReservaDAO {
    void insertar(PagoReserva p);
    List<PagoReserva> obtenerPorReserva(int idReserva);
}
