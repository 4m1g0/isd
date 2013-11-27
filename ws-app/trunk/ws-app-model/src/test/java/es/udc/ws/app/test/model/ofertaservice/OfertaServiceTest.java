package es.udc.ws.app.test.model.ofertaservice;

import static es.udc.ws.app.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.app.model.util.ModelConstants.OFERTA_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.PRECIO_REAL_MAXIMO;
import static es.udc.ws.app.model.util.ModelConstants.PRECIO_REBAJADO_MAXIMO;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PERSONAS;
import static es.udc.ws.app.model.util.ModelConstants.NUM_ESTADOS;
import static es.udc.ws.app.model.util.ModelConstants.RESERVA_EXPIRATION_DAYS;

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

import es.udc.ws.app.exceptions.ReservaExpirationException;
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
		// public Oferta(String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas, short estado) 
		return new Oferta(titulo, "Oferta description", Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 19.95F, 14.95F, (short) 5, Oferta.ESTADO_CREADA);
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

	private void removeOferta(Long ofertaId) throws InputValidationException {

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
			InstanceNotFoundException {

		Oferta oferta = getValidOferta();
		Oferta addedOferta = null;

		addedOferta = ofertaService.addOferta(oferta);
		Oferta foundOferta = ofertaService.findOferta(addedOferta.getOfertaId());

		assertEquals(addedOferta, foundOferta);

		// Clear Database
		removeOferta(addedOferta.getOfertaId());

	}

	@Test
	public void testAddInvalidOferta() throws InputValidationException {

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

			/*/ Check oferta iniReserva in the past
			exceptionCatched = false;
			oferta = getValidOferta();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 1);
			oferta.setIniReserva(calendar);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);*/
			
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

			// Check oferta precioReal <= MAX_PRICE
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setMaxPersonas((short) (PRECIO_REAL_MAXIMO + 1));
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

			// Check oferta precioRebajado <= MAX_PRICE
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setMaxPersonas((short) (PRECIO_REBAJADO_MAXIMO + 1));
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta maxPersonas >= 0
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setMaxPersonas((short) -1);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta maxPersonas <= MAX_PERSONAS
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setMaxPersonas((short) (MAX_PERSONAS + 1));
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			// Check oferta Estados
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setEstado((short) -1);
			try {
				addedOferta = ofertaService.addOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			
			// Check oferta Estados <= NUM_ESTADOS
			exceptionCatched = false;
			oferta = getValidOferta();
			oferta.setEstado((short) (NUM_ESTADOS + 1));
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
	public void testAddNullMaxPersonasOferta() throws InputValidationException, InstanceNotFoundException {
		
		Oferta oferta = createOferta(getValidOferta());
		oferta.setMaxPersonas(null);
		ofertaService.updateOferta(oferta);
		Oferta foundOferta = ofertaService.findOferta(oferta.getOfertaId());
		assertEquals(oferta, foundOferta);

		// Clear Database
		removeOferta(oferta.getOfertaId());
	}
	
	@Test
	public void testUpdateOferta() throws InputValidationException,
			InstanceNotFoundException {

		Oferta oferta = createOferta(getValidOferta());
		try {

			oferta.setTitulo("new titulo");
			oferta.setMaxPersonas((short) 5);
			oferta.setDescripcion("new description");
			oferta.setPrecioReal(20);
			oferta.setEstado(Oferta.ESTADO_LIBERADA);

			ofertaService.updateOferta(oferta);

			Oferta updatedOferta = ofertaService.findOferta(oferta.getOfertaId());
			assertEquals(oferta, updatedOferta);
			
			boolean exceptionCatched = false;
			try {
				// actualizar oferta con estado != creada
				ofertaService.updateOferta(oferta);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			// Clear Database
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidOferta() throws InputValidationException,
			InstanceNotFoundException {

		Oferta oferta = createOferta(getValidOferta());
		try {
			// Check oferta titulo not null
			oferta = ofertaService.findOferta(oferta.getOfertaId());
			oferta.setTitulo(null);
			ofertaService.updateOferta(oferta);
		} finally {
			// Clear Database
			removeOferta(oferta.getOfertaId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentOferta() throws InputValidationException,
			InstanceNotFoundException {

		Oferta oferta = getValidOferta();
		oferta.setOfertaId(NON_EXISTENT_OFERTA_ID);
		ofertaService.updateOferta(oferta);

	}

	@Test
	public void testRemoveOferta() throws InstanceNotFoundException, InputValidationException {

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
	public void testRemoveNonExistentOferta() throws InstanceNotFoundException, InputValidationException {

		ofertaService.removeOferta(NON_EXISTENT_OFERTA_ID);

	}

	@Test
	public void testFindOfertas() throws InputValidationException {

		// Add ofertas
		List<Oferta> ofertas = new LinkedList<Oferta>();
		Oferta oferta1 = createOferta(getValidOferta("oferta patata 1"));
		ofertas.add(oferta1);
		Oferta oferta2 = createOferta(getValidOferta("oferta patata 2"));
		ofertas.add(oferta2);
		Oferta oferta3 = getValidOferta("oferta patata 3");
		oferta3.setDescripcion("prueba de fuego");
		oferta3.setEstado(Oferta.ESTADO_LIBERADA);
		Calendar date = Calendar.getInstance();
		date.set(1990, 11, 1);
		oferta3.setIniReserva(date);
		oferta3 = ofertaService.addOferta(oferta3);
		ofertas.add(oferta3);

		try {
			List<Oferta> foundOfertas = ofertaService.findOfertas("patAta");
			assertEquals(3, foundOfertas.size());
			assertEquals(ofertas, foundOfertas);
			
			foundOfertas = ofertaService.findOfertas();
			assertEquals(3, foundOfertas.size());
			assertEquals(ofertas, foundOfertas);
			
			foundOfertas = ofertaService.findOfertas("patAta 2");
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(1), foundOfertas.get(0));

			foundOfertas = ofertaService.findOfertas("patata 5");
			assertEquals(0, foundOfertas.size());
			
			foundOfertas = ofertaService.findOfertas("fuEgo");
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(2), foundOfertas.get(0));
			
			Calendar date1 = Calendar.getInstance();
			date1.set(1990, 11, 7);
			foundOfertas = ofertaService.findOfertas(date1);
			assertEquals(1, foundOfertas.size());
			assertEquals(ofertas.get(2), foundOfertas.get(0));
			
			date1.set(1990, 10, 7);
			foundOfertas = ofertaService.findOfertas(date1);
			assertEquals(0, foundOfertas.size());
			
			foundOfertas = ofertaService.findOfertas("fuego", Oferta.ESTADO_LIBERADA);
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
		InputValidationException, ReservaExpirationException {
			Oferta oferta = createOferta(getValidOferta());
			try {
				Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
						oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));
				
				ofertaService.reclamarOferta(reserva.getReservaId());
				/* Clear database. */
				removeReserva(reserva.getReservaId());
			} finally {
				removeOferta(oferta.getOfertaId());
			}
	}
	
	@Test
	public void testReservarOfertaAndFindReserva() throws InstanceNotFoundException,
			InputValidationException, ReservaExpirationException {

		Oferta oferta = createOferta(getValidOferta());
		
		try {
			
			/* Buy oferta. */
			Calendar beforeIniReserva = Calendar.getInstance();
			beforeIniReserva.add(Calendar.DAY_OF_MONTH,
					RESERVA_EXPIRATION_DAYS);
			
			beforeIniReserva.set(Calendar.MILLISECOND, 0);

			Reserva reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));

			Calendar afterIniReserva = Calendar.getInstance();
			afterIniReserva
					.add(Calendar.DAY_OF_MONTH, RESERVA_EXPIRATION_DAYS);
			afterIniReserva.set(Calendar.MILLISECOND, 0);

			/* Find reserva. */
			Reserva foundReserva = ofertaService.findReserva(reserva.getReservaId());
			
			/* Check reserva. */
			assertEquals(reserva, foundReserva);
			assertEquals(VALID_CREDIT_CARD_NUMBER,
					foundReserva.getNumeroTarjeta());
			assertEquals(USER_EMAIL, foundReserva.getEmailUsuario());
			assertEquals(oferta.getOfertaId(), foundReserva.getOfertaId());
			/*assertTrue((foundReserva.getFechaReserva().compareTo(
					beforeIniReserva) >= 0));FIXME*/
			assertTrue((foundReserva.getFechaReserva().compareTo(
							afterIniReserva) <= 0));
			assertTrue(Calendar.getInstance().after(
					foundReserva.getFechaReserva()));
			/*assertTrue(foundReserva.getOfertaUrl().startsWith(
					BASE_URL + reserva.getOfertaId()));*/
			
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
			InputValidationException, ReservaExpirationException {
		Oferta oferta = createOferta(getValidOferta());
		List<Reserva> reservas = new ArrayList<Reserva>();
		List<Reserva> _reservas = new ArrayList<Reserva>();
		
		try {
			reservas.add(0, ofertaService.findReserva(ofertaService.reservarOferta(
				oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			
			reservas.add(1, ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			
			_reservas = ofertaService.findReservas(oferta.getOfertaId(), null);
			
			assertEquals(reservas.size(), _reservas.size());
			assertEquals(reservas, _reservas);
			
			/* Añadir y buscar reserva en estado cerrada */			
			reservas.add(2, ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER)));
			ofertaService.reclamarOferta(reservas.get(2).getReservaId());
			
			_reservas = ofertaService.findReservas(oferta.getOfertaId(), Reserva.ESTADO_CERRADA);
			
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
	public void testBuyOfertaWithInvalidCreditCard() throws 
		InputValidationException, InstanceNotFoundException {

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
	public void testBuyNonExistentOferta() throws InputValidationException,
			InstanceNotFoundException {

		Reserva reserva = ofertaService.findReserva(NON_EXISTENT_RESERVA_ID);
		/* Clear database. */
		removeReserva(reserva.getReservaId());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentReserva() throws InstanceNotFoundException,
			ReservaExpirationException {

		ofertaService.findReserva(NON_EXISTENT_RESERVA_ID);

	}

	/*@Test(expected = ReservaExpirationException.class)
	public void testGetExpiredOfertaUrl() throws InputValidationException,
			ReservaExpirationException, InstanceNotFoundException {

		Oferta oferta = createOferta(getValidOferta());
		Reserva reserva = null;
		try {
			reserva = ofertaService.findReserva(ofertaService.reservarOferta(
					oferta.getOfertaId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER));

			reserva.getFechaReserva().add(Calendar.DAY_OF_MONTH,
					-1 * (RESERVA_EXPIRATION_DAYS + 1));
			updateReserva(reserva);

			ofertaService.findReserva(reserva.getReservaId());
		} finally {
			// Clear Database (reserva if it was created and oferta)
			if (reserva != null) {
				removeReserva(reserva.getReservaId());
			}
			removeOferta(oferta.getOfertaId());
		}

	}*/

}
