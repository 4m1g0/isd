package es.udc.ws.app.model.ofertaservice;

import static es.udc.ws.app.model.util.ModelConstants.NUM_ESTADOS;
import static es.udc.ws.app.model.util.ModelConstants.OFERTA_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.oferta.SqlOfertaDao;
import es.udc.ws.app.model.oferta.SqlOfertaDaoFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.app.validation.PropertyValidator;

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
        PropertyValidator.validateDouble("precioReal", oferta.getPrecioReal(), 0, Float.MAX_VALUE);
        PropertyValidator.validateDouble("precioRebajado", oferta.getPrecioRebajado(), 0, Float.MAX_VALUE);
        PropertyValidator.validateDate("iniReserva", oferta.getIniReserva());
        PropertyValidator.validateDate("limReserva", oferta.getLimReserva());
        PropertyValidator.validateDate("limOferta", oferta.getLimOferta());
        
        if (oferta.getMaxPersonas() != Short.MAX_VALUE) // maxPersonas = null sin limite
        	PropertyValidator.validateLong("maxPersonas", oferta.getMaxPersonas(), 0, Short.MAX_VALUE);
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
    public void updateOferta(Long ofertaId, String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas) throws InputValidationException, InstanceNotFoundException, OfertaEstadoException {

        Oferta old = findOferta(ofertaId);
        
        if (old.getEstado() != Oferta.ESTADO_CREADA) {
        	throw new OfertaEstadoException(ofertaId, old.getEstado());
        }
        
        // Metodos set. Menos costoso que recorrer con un for e ir comparando si es distinto o no.
        old.setTitulo(titulo);
        old.setDescripcion(descripcion);
        old.setIniReserva(iniReserva);
        old.setLimReserva(limReserva);
        old.setLimOferta(limOferta);
        old.setPrecioReal(precioReal);
        old.setPrecioRebajado(precioRebajado);
        old.setMaxPersonas(maxPersonas);
        
        validateOferta(old);
        
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                ofertaDao.update(connection, old);

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
    public void removeOferta(Long ofertaId) throws InstanceNotFoundException, InputValidationException, OfertaEstadoException {
    	
    	Oferta old = findOferta(ofertaId);
        if (old.getEstado() != Oferta.ESTADO_CREADA && old.getEstado() != Oferta.ESTADO_LIBERADA) {
        	throw new OfertaEstadoException(old.getOfertaId(), old.getEstado());
        }

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
	public List<Oferta> findOfertas() {
		return findOfertas(null, null, null);
	}

	@Override
	public List<Oferta> findOfertas(String keywords, Short estado, Calendar fecha) {
		try (Connection connection = dataSource.getConnection()) {
				return ofertaDao.findByKeywords(connection, keywords, estado, fecha);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

    @Override
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException {

    	PropertyValidator.validateEmail(emailUsuario);
        PropertyValidator.validateCreditCard(numeroTarjeta);
        
    	Oferta old = findOferta(ofertaId);
    	Calendar now = Calendar.getInstance();
    	Long reservaId = null;
    	
    	if (old.getMaxPersonas()==findReservas(ofertaId, null).size()) 
        	throw new OfertaMaxPersonasException(ofertaId, old.getMaxPersonas());
    	
    	for (Reserva reserva : findReservas(ofertaId, null)) {
    		if (reserva.getEmailUsuario().equals(emailUsuario))
            	throw new OfertaEmailException(ofertaId, emailUsuario);    		
    	}
		
		if (old.getIniReserva().after(now) || old.getLimReserva().before(now)) {
        	throw new OfertaReservaDateException(ofertaId);    		
		}	
	        
	        try (Connection connection = dataSource.getConnection()) {
	
	            try {
	
	                /* Prepare connection. */
	                connection
	                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	                connection.setAutoCommit(false);
	                
	    	        if (old.getEstado() == Oferta.ESTADO_CREADA || old.getEstado() == Oferta.ESTADO_LIBERADA) 
	    	        	old.setEstado(Oferta.ESTADO_COMPROMETIDA);
	    	        
	                /* Do work. */
	                ofertaDao.update(connection, old);
	                reservaId = reservaDao.create(connection, new Reserva(ofertaId, emailUsuario,
	                        numeroTarjeta, Reserva.ESTADO_PENDIENTE, Calendar.getInstance()));
	
	                /* Commit. */
	                connection.commit();
		
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
		//}
		return reservaId;
    }

	@Override
	public List<Reserva> findReservas(Long ofertaId, Short estado)
			throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {

            List<Reserva> reservas = reservaDao.findReservas(connection, ofertaId, estado);
            
            return reservas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }	}
	
    @Override
    public Reserva findReserva(Long reservaId) 
    		throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {

            Reserva reserva = reservaDao.find(connection, reservaId);
            
            return reserva;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

	@Override
	public boolean reclamarOferta(Long reservaId) throws InstanceNotFoundException, OfertaReclamaDateException {
		
		Reserva reserva = findReserva(reservaId);
		
		if (reserva.getEstado() == Reserva.ESTADO_CERRADA)
			return false;
		
		Oferta oferta = findOferta(reserva.getOfertaId());
		Calendar now = Calendar.getInstance();
		if (oferta.getLimOferta().before(now)) {
        	throw new OfertaReclamaDateException(oferta.getOfertaId());    		
		}	
		
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
        		reserva.setEstado(Reserva.ESTADO_CERRADA);
        		reservaDao.update(connection, reserva);
        		
        		// Si todas las reservas se han disfrutado ==> Estado de la oferta liberada!
        		if (findReservas(oferta.getOfertaId(), 
        				Reserva.ESTADO_PENDIENTE).size() == 1) //1 porque aun no hicimos commit del estado de esta reserva
        			oferta.setEstado(Oferta.ESTADO_LIBERADA);
        		
        		ofertaDao.update(connection, oferta);
        		
                /* Commit. */
                connection.commit();
        		
        		return true;
        		
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
    
}
