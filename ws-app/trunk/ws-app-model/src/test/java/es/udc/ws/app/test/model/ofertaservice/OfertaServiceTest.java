package es.udc.ws.app.test.model.ofertaservice;

import static es.udc.ws.app.model.util.ModelConstants.OFERTA_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.ofertaservice.OfertaService;
import es.udc.ws.app.model.ofertaservice.OfertaServiceFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class OfertaServiceTest {

	private final long NON_EXISTENT_OFERTA_ID = -1;
	private final long NON_EXISTENT_RESERVA_ID = -1;
	private final String USER_EMAIL = "mail@gmail.es";
	private final String INVALID_USER_EMAIL = "asdfghjk";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	private static OfertaService ofertaService = null;

	private static SqlReservaDao reservaDao = null;

	
	@BeforeClass
	public static void init() {

		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this
		 * is needed to test "es.udc.ws.ofertas.model.ofertaservice.OfertaService"
		 */
		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(OFERTA_DATA_SOURCE, dataSource);

		ofertaService = OfertaServiceFactory.getService();

		reservaDao = SqlReservaDaoFactory.getDao();

	}

	private Oferta getValidOferta(String titulo) {
		Calendar before = Calendar.getInstance();
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.MINUTE, 1);
		
		Calendar lim = Calendar.getInstance();
		lim.add(Calendar.MINUTE, 2);
		
		// public Oferta(String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas, short estado) 
		return new Oferta(titulo, "Oferta description", before, after, lim, 19.95F, 14.95F, 5L, Oferta.Estado.CREADA, 0L, 0L);
	}

	private Oferta getValidOferta() {
		return getValidOferta("Oferta titulo");
	}

	private Oferta createOferta(Oferta oferta) {

		Oferta addedOferta = null;
		try {
			addedOferta = ofertaService.addOferta(oferta);
		} catch (InputValidationException e) {
			throw new  RuntimeException(e);
		}
		return addedOferta;

	}

	private void removeOferta(Long ofertaId) throws InputValidationException, OfertaEstadoException {

		try {
			ofertaService.removeOferta(ofertaId);
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	private void removeReserva(Long reservaId) {
		
		DataSource dataSource = DataSourceLocator
				.getDataSource(OFERTA_DATA_SOURCE);
		
		try (Connection connection = dataSource.getConnection()) {

			try {
	
				/* Prepare connection. */
				connection
						.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
	
				/* Do work. */
				reservaDao.remove(connection, reservaId);
				
				/* Commit. */
				connection.commit();
	
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException|Error e) {
				connection.rollback();
				throw e;
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void testAddOfertaAndFindOferta() throws InputValidationException,
			InstanceNotFoundException, OfertaEstadoException {

		Oferta oferta = getValidOferta();
		Oferta addedOferta = null;

		addedOferta = ofertaService.addOferta(oferta);
		Oferta foundOferta = ofertaService.findOferta(addedOferta.getOfertaId());

		assertEquals(addedOferta, foundOferta);

		// Clear Database
		removeOferta(addedOferta.getOfertaId());

	}

	@Test
	public void testAddInvalidOferta() throws InputValidationException, OfertaEstadoException {

		Oferta oferta = getValidOferta();
		Oferta addedOferta = null;
		boolean exceptionCatched = false;

		try {
			// Check oferta titulo not null
			oferta.setTitulo(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check oferta titulo not empty
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setTitulo("");
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check oferta descripcion not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setDescripcion(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check oferta description not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setDescripcion("");
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta iniReserva not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setIniReserva(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta limReserva not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setLimReserva(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta limOferta not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setLimOferta(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check oferta precioReal >= 0
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setPrecioReal((short) -1);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check oferta precioRebajado >= 0
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setPrecioRebajado((short) -1);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta maxPersonas >= 0
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setMaxPersonas(-1L);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta estado not null
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setEstado(null);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
		} 
		
		finally {
			if (!exceptionCatched) {
				// Clear Database
				removeOferta(addedOferta.getOfertaId());
			}
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentOferta() throws InstanceNotFoundException {

		ofertaService.findOferta(NON_EXISTENT_OFERTA_ID);

	}

	@Test
	public void testAddNullMaxPersonasOferta() throws InputValidationException, InstanceNotFoundException, OfertaEstadoException {
		
		Oferta oferta = createOferta(getValidOferta());
		oferta.setMaxPersonas(null); // Esta linea es porque no podemos pasar un null al ofertaService.update como parametro
		ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
				oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());
		Oferta foundOferta = ofertaService.findOferta(oferta.getOfertaId());
		assertEquals(oferta, foundOferta);

		// Clear Database
		removeOferta(oferta.getOfertaId());
	}
	
	@Test
	public void testUpdateOferta() throws InputValidationException,
			InstanceNotFoundException, OfertaEstadoException {

		Oferta oferta = createOferta(getValidOferta());
		
		//Asi es mas legible que poniendo el valor de los parametros directamente en el ofertaService.updateOferta
		oferta.setTitulo("new titulo");
		oferta.setDescripcion("new description");
		oferta.setPrecioReal(20);
		
		try {

			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());
			
			Oferta updatedOferta = ofertaService.findOferta(oferta.getOfertaId());
			assertEquals(oferta, updatedOferta);
			
			//Mas abajo probamos en otro metodo(testEstadoException) otro caso de update reservando una oferta e intentando actualizarla

		} finally {
			// Clear Database
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidOferta() throws InputValidationException,
			InstanceNotFoundException, OfertaEstadoException {

		Oferta oferta = createOferta(getValidOferta());
		try {
			// Check oferta titulo not null
			oferta = ofertaService.findOferta(oferta.getOfertaId());
			ofertaService.updateOferta(oferta.getOfertaId(), null, oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());
			} finally {
			// Clear Database
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentOferta() throws InputValidationException,
			InstanceNotFoundException, OfertaEstadoException {

		Oferta oferta = getValidOferta();
		oferta.setOfertaId(NON_EXISTENT_OFERTA_ID);
		ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
				oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());
	}

	@Test
	public void testRemoveOferta() throws InstanceNotFoundException, InputValidationException, OfertaEstadoException {

		Oferta oferta = createOferta(getValidOferta());
		boolean exceptionCatched = false;
		
		try {
			ofertaService.removeOferta(oferta.getOfertaId());
			ofertaService.findOferta(oferta.getOfertaId());
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentOferta() throws InstanceNotFoundException, InputValidationException, OfertaEstadoException {

		ofertaService.removeOferta(NON_EXISTENT_OFERTA_ID);

	}

	@Test
	public void testFindOfertas() throws InputValidationException, OfertaEstadoException {

		// Add ofertas
		List<Oferta> ofertas = new LinkedList<Oferta>();
		Oferta oferta1 = createOferta(getValidOferta("oferta patata 1"));
		ofertas.add(oferta1);
		Oferta oferta2 = createOferta(getValidOferta("oferta patata 2"));
		ofertas.add(oferta2);
		Oferta oferta3 = getValidOferta("oferta patata 3");
		oferta3.setDescripcion("prueba de fuego");
		oferta3.setEstado(Oferta.Estado.LIBERADA);
		Calendar date = Calendar.getInstance();
		date.set(1990, 11, 1);
		oferta3.setIniReserva(date);
		oferta3 = ofertaService.addOferta(oferta3);
		ofertas.add(oferta3);

		try {
			List<Oferta> foundOfertas = ofertaService.findOfertas("patAta", null, null);
			assertEquals(3, foundOfertas.size());
			assertEquals(ofertas, foundOfertas);
			
			foundOfertas = ofertaService.findOfertas();
			assertEquals(3, foundOfertas.size());
			assertEquals(ofertas, foundOfertas);
			
			foundOfertas = ofertaService.findOfertas("patAta 2", null, null);
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(1), foundOfertas.get(0));

			foundOfertas = ofertaService.findOfertas("patata 5", null, null);
			assertEquals(0, foundOfertas.size());
			
			foundOfertas = ofertaService.findOfertas("fuEgo", null, null);
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(2), foundOfertas.get(0));
			
			Calendar date1 = Calendar.getInstance();
			date1.set(1990, 11, 7);
			foundOfertas = ofertaService.findOfertas(null, null, date1);
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(2), foundOfertas.get(0));
			
			date1.set(1990, 10, 7);
			foundOfertas = ofertaService.findOfertas(null, null, date1);
			assertEquals(0, foundOfertas.size());
			
			foundOfertas = ofertaService.findOfertas("fuego", Oferta.Estado.LIBERADA, null);
			assertEquals(1, foundOfertas.size());
		} finally {
			// Clear Database
			for (Oferta oferta : ofertas) {
				removeOferta(oferta.getOfertaId());
			}
		}

	}

	@Test
	public void testReservar_y_Reclamar() throws InstanceNotFoundException,
		InputValidationException, OfertaEstadoException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException, OfertaReclamaDateException {
			Oferta oferta = createOferta(getValidOferta());
			try {
				Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
						oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));
				
				assertTrue(ofertaService.reclamarOferta(reserva.getReservaId()));
				assertFalse(ofertaService.reclamarOferta(reserva.getReservaId()));
				/* Clear database. */
				removeReserva(reserva.getReservaId());
			} finally {
				removeOferta(oferta.getOfertaId());
			}
	}
	
	@Test
	public void testReservarOfertaAndFindReserva() throws InstanceNotFoundException,
			InputValidationException, OfertaEstadoException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException, OfertaReclamaDateException {

		Oferta oferta = createOferta(getValidOferta());
		
		try {
			
			/* Reservar oferta. */
			Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));

			/* Find reserva. */
			Reserva foundReserva = ofertaService.findReserva(reserva.getReservaId());
			
			/* Check reserva. */
			assertEquals(reserva, foundReserva);
			assertEquals(VALID_CREDIT_CARD_NUMBER,
					foundReserva.getNumeroTarjeta());
			assertEquals(USER_EMAIL, foundReserva.getEmailUsuario());
			assertEquals(oferta.getOfertaId(), foundReserva.getOfertaId());
			assertTrue(Calendar.getInstance().after(
					foundReserva.getFechaReserva()));
			
			/* Clear database */
			//Reclamamos la oferta para cambiar su estado y poder eliminarla (Solo se pueden borrar ofertas 'Creadas' o 'Liberadas'
			ofertaService.reclamarOferta(reserva.getReservaId());
			removeReserva(reserva.getReservaId());
		} finally {
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test
	public void testReservarOfertasAndFindReservas() throws InstanceNotFoundException,
			InputValidationException, OfertaEstadoException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException, OfertaReclamaDateException {
		Oferta oferta = createOferta(getValidOferta());
		List<Reserva> reservas = new ArrayList<Reserva>();
		List<Reserva> _reservas = new ArrayList<Reserva>();
		
		try {
			reservas.add(0, ofertaService.findReserva(ofertaService.reservarOferta(
				oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			
			reservas.add(1, ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), "1"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			
			_reservas = ofertaService.findReservas(oferta.getOfertaId(), null);
			
			assertEquals(reservas.size(), _reservas.size());
			assertEquals(reservas, _reservas);
			
			/* Añadir y buscar reserva en estado cerrada */			
			reservas.add(2, ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), "2"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			ofertaService.reclamarOferta(reservas.get(2).getReservaId());
			
			_reservas = ofertaService.findReservas(oferta.getOfertaId(), Reserva.Estado.CERRADA);
			
			assertEquals(1, _reservas.size());

			for (Reserva reserva : ofertaService.findReservas(oferta.getOfertaId(), null)) {
				ofertaService.reclamarOferta(reserva.getReservaId());
				removeReserva(reserva.getReservaId());
			}
		} 
		finally {
			removeOferta(oferta.getOfertaId());
		}
	}
	
	@Test(expected = InputValidationException.class)
	public void testReservarOfertaWithInvalidUserMail() throws 
		InputValidationException, InstanceNotFoundException, OfertaEstadoException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException {

		Oferta oferta = createOferta(getValidOferta());
		try {
			Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), INVALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER));
			
			/* Clear database. */
			removeReserva(reserva.getReservaId());
		} finally {
			removeOferta(oferta.getOfertaId());
		}

	}
	
	@Test(expected = InputValidationException.class)
	public void testReservarOfertaWithInvalidCreditCard() throws 
		InputValidationException, InstanceNotFoundException, OfertaEstadoException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException {

		Oferta oferta = createOferta(getValidOferta());
		try {
			Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, INVALID_CREDIT_CARD_NUMBER));
			
			/* Clear database. */
			removeReserva(reserva.getReservaId());
		} finally {
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testReservarNonExistentOferta() throws InputValidationException,
			InstanceNotFoundException {

		Reserva reserva = ofertaService.findReserva(NON_EXISTENT_RESERVA_ID);
		/* Clear database. */
		removeReserva(reserva.getReservaId());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentReserva() throws InstanceNotFoundException {
		ofertaService.findReserva(NON_EXISTENT_RESERVA_ID);
	}
	
	//iniReserva > limReserva
	@Test(expected = InputValidationException.class)
	public void testSetDatesWrong1() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {

		Oferta oferta = createOferta(getValidOferta());
		
		try {
			
			Calendar before = Calendar.getInstance();
			before.add(Calendar.DAY_OF_MONTH, 5);		
			oferta.setIniReserva(before);
			
			Calendar after = Calendar.getInstance();
			after.add(Calendar.DAY_OF_MONTH, 3);
			oferta.setLimReserva(after);
			
			Calendar lim = Calendar.getInstance();
			lim.add(Calendar.DAY_OF_MONTH, 6);
			oferta.setLimOferta(lim);			

			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());

		} 
		finally {
			/* Clear database. */
			removeOferta(oferta.getOfertaId());
		}
	}
	
	//iniReserva > limOferta
	@Test(expected = InputValidationException.class)
	public void testSetDatesWrong2() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {

		Oferta oferta = createOferta(getValidOferta());
		
		try {
			
			Calendar before = Calendar.getInstance();
			before.add(Calendar.DAY_OF_MONTH, 2);		
			oferta.setIniReserva(before);
			
			Calendar after = Calendar.getInstance();
			after.add(Calendar.DAY_OF_MONTH, 3);
			oferta.setLimReserva(after);
			
			Calendar lim = Calendar.getInstance();
			lim.add(Calendar.DAY_OF_MONTH, 1);
			oferta.setLimOferta(lim);			

			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());

		} 
		finally {
			/* Clear database. */
			removeOferta(oferta.getOfertaId());
		}
	}
	
	// limOferta > limReserva
	@Test(expected = InputValidationException.class)
	public void testSetDatesWrong3() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {

		Oferta oferta = createOferta(getValidOferta());
		
		try {
			
			Calendar before = Calendar.getInstance();
			before.add(Calendar.DAY_OF_MONTH, 1);		
			oferta.setIniReserva(before);
			
			Calendar after = Calendar.getInstance();
			after.add(Calendar.DAY_OF_MONTH, 3);
			oferta.setLimReserva(after);
			
			Calendar lim = Calendar.getInstance();
			lim.add(Calendar.DAY_OF_MONTH, 2);
			oferta.setLimOferta(lim);			

			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());

		} 
		finally {
			/* Clear database. */
			removeOferta(oferta.getOfertaId());
		}
	}
	
	//@Test(expected = OfertaMaxPersonasException.class)
	public void testMaxPersonasException() throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {
		Oferta oferta = createOferta(getValidOferta());
		try {
			ofertaService.reservarOferta(oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			ofertaService.reservarOferta(oferta.getOfertaId(), "a"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			ofertaService.reservarOferta(oferta.getOfertaId(), "b"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			ofertaService.reservarOferta(oferta.getOfertaId(), "c"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			ofertaService.reservarOferta(oferta.getOfertaId(), "d"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			ofertaService.reservarOferta(oferta.getOfertaId(), "e"+USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
		} finally {
			/* Clear database. */
			for (Reserva reserva : ofertaService.findReservas(oferta.getOfertaId(), null)) {
				ofertaService.reclamarOferta(reserva.getReservaId());
				removeReserva(reserva.getReservaId());
			}
			removeOferta(oferta.getOfertaId());
		}
	}
	
	@Test(expected = OfertaEstadoException.class)
	public void testEstadoException() throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {
		Oferta oferta = createOferta(getValidOferta());
		Reserva reserva = null;
		try {
			reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));
			
			oferta.setMaxPersonas(2L);
			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());			
			/* Clear database. */
		} finally {
			ofertaService.reclamarOferta(reserva.getReservaId());
			removeReserva(reserva.getReservaId());
			removeOferta(oferta.getOfertaId());
		}
	}
	
	@Test(expected = OfertaEmailException.class)
	public void testEmailException() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {
		Oferta oferta = createOferta(getValidOferta());
		try {
			ofertaService.reservarOferta(oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			
			ofertaService.reservarOferta(oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			
			/* Clear database. */
		} 
		finally {
			for (Reserva reserva : ofertaService.findReservas(oferta.getOfertaId(), null)) {
				ofertaService.reclamarOferta(reserva.getReservaId());
				removeReserva(reserva.getReservaId());
			}
			removeOferta(oferta.getOfertaId());
		}
	}
	
	@Test(expected = OfertaReservaDateException.class)
	public void testReservaDateException() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {
		
		Oferta oferta = createOferta(getValidOferta());
		Oferta oferta2 = null;

		try {

			//
			Calendar before = Calendar.getInstance();
			before.add(Calendar.DAY_OF_MONTH, 2);		
			oferta.setIniReserva(before);
			
			//Estos dos Calendar para que tenga coherencia y no salten excepciones al añadir la oferta
			Calendar after = Calendar.getInstance();
			after.add(Calendar.DAY_OF_MONTH, 3);
			oferta.setLimReserva(after);
			
			Calendar lim = Calendar.getInstance();
			lim.add(Calendar.DAY_OF_MONTH, 4);
			oferta.setLimOferta(lim);
			oferta2 = ofertaService.addOferta(oferta);

			ofertaService.reservarOferta(
					oferta2.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
			//Vamos a limpiar la segunda oferta en el siguiente metodo donde solo se reclama
			
			/* Clear database. */
		} 
		finally {

			removeOferta(oferta.getOfertaId());
			removeOferta(oferta2.getOfertaId());
		}
	}
	
	//Test comentado porque no podemos cambiar las fechas de una oferta reservada para que se reclame
	 // y tampoco antes de reservarse porque iniReserva <= limReserva <= limOferta
	// En resumen para comprobarlo habria que esperar a que en la oferta caducara su limite para disfrutarla
	//@Test(expected = OfertaReclamaDateException.class)
	public void testReclamaDateException() throws InstanceNotFoundException, InputValidationException, OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaEstadoException, OfertaReclamaDateException {

		Oferta oferta = createOferta(getValidOferta());
		Reserva reserva = null;
		
		try {

			reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));
			
			Calendar before = Calendar.getInstance();
			before.add(Calendar.DAY_OF_MONTH, -4);		
			oferta.setIniReserva(before);
			
			Calendar after = Calendar.getInstance();
			after.add(Calendar.DAY_OF_MONTH, -3);
			oferta.setLimReserva(after);
			
			Calendar lim = Calendar.getInstance();
			lim.add(Calendar.DAY_OF_MONTH, -2);
			oferta.setLimOferta(lim);			

			ofertaService.updateOferta(oferta.getOfertaId(),oferta.getTitulo(),oferta.getDescripcion(),oferta.getIniReserva(),
					oferta.getLimReserva(),oferta.getLimOferta(),oferta.getPrecioReal(),oferta.getPrecioRebajado(),oferta.getMaxPersonas());

			ofertaService.reclamarOferta(reserva.getReservaId());
			removeReserva(reserva.getReservaId());
		} 
		finally {
			/* Clear database. */
			removeOferta(oferta.getOfertaId());
		}
	}
}
