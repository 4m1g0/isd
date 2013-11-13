package es.udc.ws.app.model.ofertaservice;

import static es.udc.ws.app.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.app.model.util.ModelConstants.OFERTA_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.PRECIO_REAL_MAXIMO;
import static es.udc.ws.app.model.util.ModelConstants.PRECIO_REBAJADO_MAXIMO;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PERSONAS;
import static es.udc.ws.app.model.util.ModelConstants.NUM_ESTADOS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.oferta.SqlOfertaDao;
import es.udc.ws.app.model.oferta.SqlOfertaDaoFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class OfertaServiceImpl implements OfertaService {
    /*
     * IMPORTANT: Some JDBC drivers require "setTransactionIsolation" to
     * be called before "setAutoCommit".
     */

    private DataSource dataSource;
    private SqlOfertaDao ofertaDao = null;
    private SqlReservaDao reservaDao = null;

    public OfertaServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(OFERTA_DATA_SOURCE);
        ofertaDao = SqlOfertaDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private void validateOferta(Oferta oferta) throws InputValidationException {

        PropertyValidator.validateMandatoryString("titulo", oferta.getTitulo());
        PropertyValidator.validateMandatoryString("descripcion", oferta.getDescripcion());
        PropertyValidator.validateDouble("precioReal", oferta.getPrecioReal(), 0, PRECIO_REAL_MAXIMO);
        PropertyValidator.validateDouble("precioRebajado", oferta.getPrecioReal(), 0, PRECIO_REBAJADO_MAXIMO);
        PropertyValidator.validatePastDate("iniReserva", oferta.getIniReserva());
        PropertyValidator.validatePastDate("limReserva", oferta.getLimReserva());
        PropertyValidator.validatePastDate("limOferta", oferta.getLimOferta());
        PropertyValidator.validateLong("maxPersonas", oferta.getMaxPersonas(), 0, MAX_PERSONAS);
        PropertyValidator.validateLong("estado", oferta.getEstado(), 0, NUM_ESTADOS);
    }

    @Override
    public Oferta addOferta(Oferta oferta) throws InputValidationException {

        validateOferta(oferta);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Oferta createdOferta = ofertaDao.create(connection, oferta);

                /* Commit. */
                connection.commit();

                return createdOferta;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateOferta(Oferta oferta) throws InputValidationException,
            InstanceNotFoundException {

        validateOferta(oferta);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                ofertaDao.update(connection, oferta);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeOferta(Long ofertaId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                ofertaDao.remove(connection, ofertaId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Oferta findOferta(Long ofertaId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return ofertaDao.find(connection, ofertaId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Oferta> findOfertas(String keywords) {

        try (Connection connection = dataSource.getConnection()) {
            return ofertaDao.findByKeywords(connection, keywords);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*@Override
    public Reserva buyOferta(Long ofertaId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException {

        PropertyValidator.validateCreditCard(creditCardNumber);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. *//* FIXME
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. *//* FIXME
                Oferta oferta = ofertaDao.find(connection, ofertaId);
                Calendar expirationDate = Calendar.getInstance();
                expirationDate.add(Calendar.DAY_OF_MONTH, SALE_EXPIRATION_DAYS);
                Reserva reserva = reservaDao.create(connection, new Reserva(ofertaId, userId,
                        expirationDate, creditCardNumber, oferta.getPrice(),
                        getOfertaUrl(ofertaId), Calendar.getInstance()));

                /* Commit. *//* FIXME
                connection.commit();

                return reserva;

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Reserva findReserva(Long reservaId) throws InstanceNotFoundException,
            ReservaExpirationException {

        try (Connection connection = dataSource.getConnection()) {

            Reserva reserva = reservaDao.find(connection, reservaId);
            Calendar now = Calendar.getInstance();
            if (reserva.getExpirationDate().after(now)) {
                return reserva;
            } else {
                throw new ReservaExpirationException(reservaId,
                        reserva.getExpirationDate());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static String getOfertaUrl(Long ofertaId) {
        return BASE_URL + ofertaId + "/" + UUID.randomUUID().toString();
    }*/
}
