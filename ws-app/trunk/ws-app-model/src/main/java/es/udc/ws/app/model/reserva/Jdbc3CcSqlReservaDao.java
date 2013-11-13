package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao {

    @Override
    public Reserva create(Connection connection, Reserva sale) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Reserva"
                + " (ofertaId, emailUsuario, numeroTarjeta, estado, fechaReserva) VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, sale.getOfertaId());
            preparedStatement.setString(i++, sale.getEmailUsuario());
            preparedStatement.setString(i++, sale.getNumeroTarjeta());
            preparedStatement.setShort(i++, sale.getEstado());
            Timestamp fechaReserva = new Timestamp(sale.getFechaReserva().getTime()
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
            Long reservaId = resultSet.getLong(1);

            /* Return sale. */
            return new Reserva(reservaId, sale.getOfertaId(), sale.getEmailUsuario(),
                    sale.getNumeroTarjeta(), sale.getEstado(), sale.getFechaReserva());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
