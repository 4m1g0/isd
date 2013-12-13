package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao {

    @Override
    public Long create(Connection connection, Reserva reserva) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Reserva"
                + " (ofertaId, emailUsuario, numeroTarjeta, estado, fechaReserva) VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserva.getOfertaId());
            preparedStatement.setString(i++, reserva.getEmailUsuario());
            preparedStatement.setString(i++, reserva.getNumeroTarjeta());
            preparedStatement.setShort(i++, reserva.getEstado());
            Timestamp fechaReserva = new Timestamp(reserva.getFechaReserva().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, fechaReserva);

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            
        return resultSet.getLong(1);

            /* Return reserva. *//*
            return new Reserva(reservaId, reserva.getOfertaId(), reserva.getEmailUsuario(),
                    reserva.getNumeroTarjeta(), reserva.getEstado(), reserva.getFechaReserva());*/

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
