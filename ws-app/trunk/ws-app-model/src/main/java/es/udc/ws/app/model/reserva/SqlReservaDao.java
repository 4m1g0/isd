package es.udc.ws.app.model.reserva;

import java.sql.Connection;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlReservaDao {

    public Reserva create(Connection connection, Reserva sale);

    public Reserva find(Connection connection, Long saleId)
            throws InstanceNotFoundException;

    public void update(Connection connection, Reserva sale)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long saleId)
            throws InstanceNotFoundException;
}
