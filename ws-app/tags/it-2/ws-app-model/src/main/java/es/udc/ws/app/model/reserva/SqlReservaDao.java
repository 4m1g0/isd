package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlReservaDao {

    public Long create(Connection connection, Reserva reserva);

    public Reserva find(Connection connection, Long reservaId)
            throws InstanceNotFoundException;

    public List<Reserva> findReservas(Connection connection, Long ofertaId, Short estado)
            throws InstanceNotFoundException;
    
    public void update(Connection connection, Reserva reserva)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long reservaId)
            throws InstanceNotFoundException;
}
