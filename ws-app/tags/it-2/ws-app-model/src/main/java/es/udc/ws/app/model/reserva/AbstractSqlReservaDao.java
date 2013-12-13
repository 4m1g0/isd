package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlReservaDao implements SqlReservaDao {

    protected AbstractSqlReservaDao() {
    }

    @Override
    public Reserva find(Connection connection, Long reservaId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT ofertaId, emailUsuario, numeroTarjeta, estado, fechaReserva FROM Reserva WHERE reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservaId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(reservaId,
                        Reserva.class.getName());
            }

            /* Get results. */
            i = 1;
            Long ofertaId = resultSet.getLong(i++);
            String emailUsuario = resultSet.getString(i++);
            String numeroTarjeta = resultSet.getString(i++);
            short estado = resultSet.getShort(i++);
            Calendar fechaReserva = Calendar.getInstance();
            fechaReserva.setTime(resultSet.getTimestamp(i++));

            /* Return reserva. */
            return new Reserva(reservaId, ofertaId, emailUsuario, numeroTarjeta,
                    estado, fechaReserva);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

	@Override
	public List<Reserva> findReservas(Connection connection, Long ofertaId, Short estado)
			throws InstanceNotFoundException {
		
        /* Create "queryString". */
        String queryString = "SELECT reservaId, emailUsuario, numeroTarjeta, estado, fechaReserva FROM Reserva WHERE ofertaId = ?";		
		if (estado != null)
			queryString += " AND estado = ?";
			
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, ofertaId.longValue());
            if (estado != null)
                preparedStatement.setShort(i++, estado);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reserva> reservas = new ArrayList<Reserva>();

            /* Get results. */
            while (resultSet.next()) {
	            i = 1;
	            Long reservaId = resultSet.getLong(i++);
	            String emailUsuario = resultSet.getString(i++);
	            String numeroTarjeta = resultSet.getString(i++);
	            short _estado = resultSet.getShort(i++);
	            Calendar fechaReserva = Calendar.getInstance();
	            fechaReserva.setTime(resultSet.getTimestamp(i++));

	            reservas.add(new Reserva(reservaId, ofertaId, emailUsuario, numeroTarjeta, _estado, fechaReserva));
            }
            
            /* Return reserva. */
            return reservas;
            		
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
             
	}
	
    @Override
    public void update(Connection connection, Reserva reserva)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Reserva"
                + " SET ofertaId = ?, emailUsuario = ?, numeroTarjeta = ?, "
                + " estado = ?, fechaReserva = ? WHERE reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserva.getOfertaId());
            preparedStatement.setString(i++, reserva.getEmailUsuario());
            preparedStatement.setString(i++, reserva.getNumeroTarjeta());
            preparedStatement.setShort(i++, reserva.getEstado());
            Timestamp date = reserva.getFechaReserva() != null ? new Timestamp(
                    reserva.getFechaReserva().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setLong(i++, reserva.getReservaId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(reserva.getOfertaId(),
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long reservaId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Reserva WHERE" + " reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservaId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reservaId,
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
