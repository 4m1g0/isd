package es.udc.ws.app.model.oferta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlOfertaDao extends AbstractSqlOfertaDao {

    @Override
    public Oferta create(Connection connection, Oferta oferta) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Oferta"
                + " (titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, oferta.getTitulo());
            preparedStatement.setString(i++, oferta.getDescripcion());
            Timestamp date = oferta.getIniReserva() != null ? new Timestamp(
                    oferta.getIniReserva().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date);
            Timestamp date2 = oferta.getLimReserva() != null ? new Timestamp(
                    oferta.getLimReserva().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date2);
            Timestamp date3 = oferta.getLimOferta() != null ? new Timestamp(
                    oferta.getLimOferta().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date3);
            preparedStatement.setFloat(i++, oferta.getPrecioReal());
            preparedStatement.setFloat(i++, oferta.getPrecioRebajado());
            preparedStatement.setShort(i++, oferta.getMaxPersonas());
            preparedStatement.setShort(i++,  oferta.getEstado());
            preparedStatement.setLong(i++, oferta.getOfertaId());
            
            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long ofertaId = resultSet.getLong(1);

            /* Return movie. */
            return new Oferta(ofertaId, oferta.getTitulo(), oferta.getDescripcion(),
                    oferta.getIniReserva(), oferta.getLimReserva(), oferta.getLimOferta(), oferta.getPrecioReal(), 
                    oferta.getPrecioRebajado(), oferta.getMaxPersonas(), oferta.getEstado());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
