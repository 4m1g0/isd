package es.udc.ws.app.model.oferta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.oferta.Oferta.Estado;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

/**
 * A partial implementation of
 * <code>SQLOfertaDAO</code> that leaves
 * <code>create(Connection, Oferta)</code> as abstract.
 */
public abstract class AbstractSqlOfertaDao implements SqlOfertaDao {

    protected AbstractSqlOfertaDao() {
    }

    @Override
    public Oferta find(Connection connection, Long ofertaId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado, numReservas, numUsedReservas FROM Oferta WHERE ofertaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, ofertaId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(ofertaId,
                        Oferta.class.getName());
            }

            /* Get results. */
            i = 1;
            String titulo = resultSet.getString(i++);
            String descripcion = resultSet.getString(i++);
            Calendar iniReserva = Calendar.getInstance();
            iniReserva.setTime(resultSet.getTimestamp(i++));
            Calendar limReserva = Calendar.getInstance();
            limReserva.setTime(resultSet.getTimestamp(i++));
            Calendar limOferta = Calendar.getInstance();
            limOferta.setTime(resultSet.getTimestamp(i++));
            float precioReal = resultSet.getFloat(i++);
            float precioRebajado = resultSet.getFloat(i++);
            Long maxPersonas = resultSet.getLong(i++);
            if (maxPersonas == 0) //resultSet.getLong devuelve un 0 si hay un null en la base de datos
            	maxPersonas = Long.MAX_VALUE;
            Estado estado = Estado.valueOf(resultSet.getString(i++));
            Long numReservas = resultSet.getLong(i++);
            Long numUsedReservas = resultSet.getLong(i++);

            /* Return oferta. */
            return new Oferta(ofertaId, titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado, numReservas, numUsedReservas);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Oferta> findByKeywords(Connection connection, String keywords, Estado estadoRequerido, Calendar fecha) {

    	boolean and = false;
        /* Create "queryString". */
        String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT ofertaId, titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado, numReservas, numUsedReservas FROM Oferta";
        if (words != null && words.length > 0) {
            queryString += " WHERE";
            and = true;
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " AND";
                }
                queryString += " LOWER(CONCAT(descripcion, titulo)) LIKE LOWER(?)";
            }
        }
        if (estadoRequerido != null) {
        	if (and)
        		queryString += " AND estado = ?";
        	else {
        		queryString += " WHERE estado = ?";
        		and = true;
        	}	
        }
        if (fecha != null) {
        	if (and)
        		queryString += " AND ? BETWEEN iniReserva AND limReserva";
        	else{
        		queryString += " WHERE ? BETWEEN iniReserva AND limReserva";
        	}	
        }
        queryString += " ORDER BY titulo";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
        	int i = 0;
            if (words != null) {
                /* Fill "preparedStatement". */
                for (i = 0; i < words.length; i++) {
                    preparedStatement.setString(i + 1, "%" + words[i] + "%");
                }
            }
            if (estadoRequerido != null) {
            	preparedStatement.setString(++i, estadoRequerido.name());
            }
            if (fecha != null) {
            	preparedStatement.setTimestamp(++i, new Timestamp(fecha.getTime().getTime()));
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read ofertas. */
            List<Oferta> ofertas = new ArrayList<Oferta>();

            while (resultSet.next()) {

                i = 1;
                Long ofertaId = new Long(resultSet.getLong(i++));
                String titulo = resultSet.getString(i++);
                String descripcion = resultSet.getString(i++);
                Calendar iniReserva = Calendar.getInstance();
                iniReserva.setTime(resultSet.getTimestamp(i++));
                Calendar limReserva = Calendar.getInstance();
                limReserva.setTime(resultSet.getTimestamp(i++));
                Calendar limOferta = Calendar.getInstance();
                limOferta.setTime(resultSet.getTimestamp(i++));
                float precioReal = resultSet.getFloat(i++);
                float precioRebajado = resultSet.getFloat(i++);
                Long maxPersonas = resultSet.getLong(i++);
                if (maxPersonas == 0) //resultSet.getLong devuelve un 0 si hay un null en la base de datos
                	maxPersonas = Long.MAX_VALUE;
                Estado estado = Estado.valueOf(resultSet.getString(i++));
                Long numReservas = resultSet.getLong(i++);
                Long numUsedReservas = resultSet.getLong(i++);

                ofertas.add(new Oferta(ofertaId, titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado, numReservas, numUsedReservas));
            }

            /* Return ofertas. */
            return ofertas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Oferta oferta)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Oferta"
                + " SET titulo = ?, descripcion = ?, precioReal = ?, precioRebajado = ?, maxPersonas = ?, estado = ?, numReservas = ?, numUsedReservas = ? WHERE ofertaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, oferta.getTitulo());
            preparedStatement.setString(i++, oferta.getDescripcion());
            preparedStatement.setFloat(i++, oferta.getPrecioReal());
            preparedStatement.setFloat(i++, oferta.getPrecioRebajado());
            if (oferta.getMaxPersonas() == Long.MAX_VALUE)
                preparedStatement.setObject(i++, null);
            else
            	preparedStatement.setLong(i++, oferta.getMaxPersonas());
            preparedStatement.setString(i++, oferta.getEstado().name());
            preparedStatement.setLong(i++, oferta.getNumReservas());
            preparedStatement.setLong(i++, oferta.getNumUsedReservas());
            preparedStatement.setLong(i++, oferta.getOfertaId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(oferta.getOfertaId(),
                        Oferta.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long ofertaId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Oferta WHERE" + " ofertaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, ofertaId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(ofertaId,
                        Oferta.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
