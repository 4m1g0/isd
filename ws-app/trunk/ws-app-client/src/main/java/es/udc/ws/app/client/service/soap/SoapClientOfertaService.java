package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.ClientOfertaService;
import es.udc.ws.app.client.service.soap.wsdl.*;
import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.ReservaExpirationException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;
import javax.xml.ws.BindingProvider;

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
            throws InputValidationException, InstanceNotFoundException {
        try {
            ofertasProvider.updateOferta(OfertaDtoToSoapOfertaDtoConversor
                    .toSoapOfertaDto(oferta));
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeOferta(Long ofertaId)
            throws InstanceNotFoundException {
        try {
            ofertasProvider.removeOferta(ofertaId);
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
    }

    @Override
    public List<OfertaDto> findOfertas(String keywords, Short estado, Calendar fecha) {
        return OfertaDtoToSoapOfertaDtoConversor.toOfertaDtos(
                    ofertasProvider.findOfertas(keywords, estado, fecha));
    }

    @Override
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException {
        try {
            return ofertasProvider.reservarOferta(ofertaId, emailUsuario, numeroTarjeta);
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getOfertaUrl(Long reservaId)
            throws InstanceNotFoundException, ReservaExpirationException {
        try {
            return ofertasProvider.findReserva(reservaId).getOfertaUrl();
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } catch (SoapReservaExpirationException ex) {
            throw new ReservaExpirationException(ex.getFaultInfo().getReservaId(),
                    ex.getFaultInfo().getExpirationDate()
                    .toGregorianCalendar());
        }
    }

	@Override
	public List<ReservaDto> findReservas(Long ofertaId, Short estado)
			throws InstanceNotFoundException {
        return OfertaDtoToSoapOfertaDtoConversor.toOfertaDtos(
                ofertasProvider.findReservas(ofertaId, estado));
	}
	
	@Override
	public ReservaDto findReserva(Long reservaId)
			throws InstanceNotFoundException {
		return OfertaDtoToSoapOfertaDtoConversor.toOfertaDto(
				ofertasProvider.findReserva(reservaId));
	}

	@Override
	public boolean reclamarOferta(Long reservaId)
			throws InstanceNotFoundException {
        try {
            return ofertasProvider.reclamarOferta(reservaId);
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } catch(Exception ex) {
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
