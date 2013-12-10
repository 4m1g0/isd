package es.udc.ws.app.client.service.soap;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;

import es.udc.ws.app.client.service.ClientOfertaService;
import es.udc.ws.app.client.service.soap.wsdl.OfertasProvider;
import es.udc.ws.app.client.service.soap.wsdl.OfertasProviderService;
import es.udc.ws.app.client.service.soap.wsdl.SoapInputValidationException;
import es.udc.ws.app.client.service.soap.wsdl.SoapInstanceNotFoundException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOfertaEmailException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOfertaEstadoException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOfertaMaxPersonasException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOfertaReclamaDateException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOfertaReservaDateException;
import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class SoapClientOfertaService implements ClientOfertaService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
        "SoapClientOfertaService.endpointAddress";

    private String endpointAddress;

    private OfertasProvider ofertasProvider;

    public SoapClientOfertaService() {
        init(getEndpointAddress());
    }

    private void init(String ofertasProviderURL) {
        OfertasProviderService stockQuoteProviderService =
                new OfertasProviderService();
        ofertasProvider = stockQuoteProviderService
                .getOfertasProviderPort();
        ((BindingProvider) ofertasProvider).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                ofertasProviderURL);
    }

    @Override
    public Long addOferta(OfertaDto oferta)
            throws InputValidationException {
        try {
            return ofertasProvider.addOferta(OfertaDtoToSoapOfertaDtoConversor
                    .toSoapOfertaDto(oferta));
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateOferta(OfertaDto oferta)
            throws InputValidationException, InstanceNotFoundException, OfertaEstadoException {
        try {
            ofertasProvider.updateOferta(OfertaDtoToSoapOfertaDtoConversor
                    .toSoapOfertaDto(oferta));
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } 
        catch (SoapOfertaEstadoException ex) {
            throw new OfertaEstadoException(ex.getFaultInfo().getOfertaId(),
                    ex.getFaultInfo().getEstado());
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeOferta(Long ofertaId)
            throws InstanceNotFoundException, InputValidationException, OfertaEstadoException {
        try {
            ofertasProvider.removeOferta(ofertaId);
        }
        catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        }
        catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
        catch (SoapOfertaEstadoException ex) {
            throw new OfertaEstadoException(ex.getFaultInfo().getOfertaId(),
                    ex.getFaultInfo().getEstado());
        }
    }

	@Override
	public OfertaDto findOferta(Long ofertaId) throws InstanceNotFoundException {
		try {
			return OfertaDtoToSoapOfertaDtoConversor.toOfertaDto(
					ofertasProvider.findOferta(ofertaId));
		}
        catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
	}

    @Override
    public List<OfertaDto> findOfertas(String keywords, Calendar fecha) {
        GregorianCalendar c1 = new GregorianCalendar();
        try {
			return OfertaDtoToSoapOfertaDtoConversor.toOfertaDtos(
			            ofertasProvider.findOfertas(keywords, DatatypeFactory.newInstance().newXMLGregorianCalendar(c1)));
		} catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException {
        try {
            return ofertasProvider.reservarOferta(ofertaId, emailUsuario, numeroTarjeta);
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
        catch (SoapOfertaMaxPersonasException ex) {
            throw new OfertaMaxPersonasException(ex.getFaultInfo().getOfertaId(),
                    ex.getFaultInfo().getMaxPersonas());
        }
        catch (SoapOfertaEmailException ex) {
            throw new OfertaEmailException(ex.getFaultInfo().getOfertaId(),
                    ex.getFaultInfo().getEmailUsuario());
        }
        catch (SoapOfertaReservaDateException ex) {
            throw new OfertaReservaDateException(ex.getFaultInfo().getOfertaId());
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

	@Override
	public List<ReservaDto> findReservas(Long ofertaId, Short estado)
			throws InstanceNotFoundException {
		try {
	        return OfertaDtoToSoapOfertaDtoConversor.toReservaDtos(
	                ofertasProvider.findReservas(ofertaId, estado));
		}
        catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
	}
	
	@Override
	public ReservaDto findReserva(Long reservaId)
			throws InstanceNotFoundException {
		try {
			return OfertaDtoToSoapOfertaDtoConversor.toReservaDto(
					ofertasProvider.findReserva(reservaId));
		}
        catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
	}

	@Override
	public boolean reclamarOferta(Long reservaId)
			throws InstanceNotFoundException, OfertaReclamaDateException, OfertaReservaDateException {
        try {
            return ofertasProvider.reclamarOferta(reservaId);
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
        catch (SoapOfertaReclamaDateException ex) {
            throw new OfertaReservaDateException(ex.getFaultInfo().getOfertaId());
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
	}
	
    private String getEndpointAddress() {

        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(
                ENDPOINT_ADDRESS_PARAMETER);
        }

        return endpointAddress;
    }
}
