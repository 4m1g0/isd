package es.udc.ws.app.soapservice;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.dto.ReservaDto.Estado;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.exceptions.ReservaEstadoException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.ofertaservice.OfertaServiceFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.serviceutil.OfertaToOfertaDtoConversor;
import es.udc.ws.app.serviceutil.ReservaToReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(
    name="OfertasProvider",
    serviceName="OfertasProviderService",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaService {

    @WebMethod(
        operationName="addOferta"
    )
    public Long addOferta(@WebParam(name="ofertaDto") OfertaDto ofertaDto)
            throws SoapInputValidationException {
        try {
            Oferta oferta = OfertaToOfertaDtoConversor.toOferta(ofertaDto);
            
            return OfertaServiceFactory.getService().addOferta(oferta).getOfertaId();
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }
    }

    @WebMethod(
        operationName="updateOferta"
    )
    public void updateOferta(@WebParam(name="ofertaDto") OfertaDto ofertaDto)
            throws SoapInputValidationException, SoapInstanceNotFoundException, SoapOfertaEstadoException {
        try {
            OfertaServiceFactory.getService().updateOferta(ofertaDto.getOfertaId(), ofertaDto.getTitulo(), ofertaDto.getDescripcion(),
            		ofertaDto.getIniReserva(), ofertaDto.getLimReserva(), ofertaDto.getLimOferta(), ofertaDto.getPrecioReal(),
            		ofertaDto.getPrecioRebajado(), ofertaDto.getMaxPersonas());
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        }
         catch (OfertaEstadoException ex) {
             throw new SoapOfertaEstadoException(
                     new SoapOfertaEstadoExceptionInfo(ex.getOfertaId(),
                         ex.getEstado()));
         }
    }

    @WebMethod(
        operationName="removeOferta"
    )
    public void removeOferta(@WebParam(name="ofertaId") Long ofertaId)
            throws SoapInstanceNotFoundException, SoapInputValidationException, SoapOfertaEstadoException {
        try {
            OfertaServiceFactory.getService().removeOferta(ofertaId);
        } 
        catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(
                    ex.getInstanceId(), ex.getInstanceType()));
        }
        catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }
        catch (OfertaEstadoException ex) {
            throw new SoapOfertaEstadoException(
                    new SoapOfertaEstadoExceptionInfo(ex.getOfertaId(),
                        ex.getEstado()));
        }
    }

    @WebMethod(
            operationName="findOferta"
        )
        public OfertaDto findOferta(@WebParam(name="ofertaId") Long ofertaId)
                throws SoapInstanceNotFoundException {

            try {
                Oferta oferta = OfertaServiceFactory.getService().findOferta(ofertaId);
                return OfertaToOfertaDtoConversor.toOfertaDto(oferta);
            } catch (InstanceNotFoundException ex) {
                throw new SoapInstanceNotFoundException(
                        new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                            ex.getInstanceType()));
            }
        }
            
    @WebMethod(
        operationName="findOfertas"
    )
    public List<OfertaDto> findOfertas(
            @WebParam(name="keywords") String keywords) {
        List<Oferta> ofertas =
                OfertaServiceFactory.getService().findOfertas(keywords, null, Calendar.getInstance());
        return OfertaToOfertaDtoConversor.toOfertaDtos(ofertas);
    }

    @WebMethod(
        operationName="reservarOferta"
    )
    public Long reservarOferta(@WebParam(name="ofertaId")  Long ofertaId,
                         @WebParam(name="emailUsuario")   String emailUsuario,
                         @WebParam(name="numeroTarjeta") String numeroTarjeta)
            throws SoapInstanceNotFoundException, SoapInputValidationException, SoapOfertaMaxPersonasException, SoapOfertaEmailException, SoapOfertaReservaDateException {
        try {
            Reserva reserva = OfertaServiceFactory.getService().findReserva(OfertaServiceFactory.getService()
                    .reservarOferta(ofertaId, emailUsuario, numeroTarjeta));
            return reserva.getReservaId();
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }
        catch (OfertaMaxPersonasException ex) {
            throw new SoapOfertaMaxPersonasException(
                    new SoapOfertaMaxPersonasExceptionInfo(ex.getOfertaId(),
                        ex.getMaxPersonas()));
        }
        catch (OfertaEmailException ex) {
            throw new SoapOfertaEmailException(
                    new SoapOfertaEmailExceptionInfo(ex.getOfertaId(),
                        ex.getEmailUsuario()));
        }
        catch (OfertaReservaDateException ex) {
            throw new SoapOfertaReservaDateException(
                    new SoapOfertaReservaDateExceptionInfo(ex.getOfertaId()));
        }
    }

    @WebMethod(
            operationName="findReservas"
        )
        public List<ReservaDto> findReservas(
                @WebParam(name="ofertaId") Long ofertaId,
                @WebParam(name="estado") Estado estado) throws SoapInstanceNotFoundException {
    	try {
    		List<Reserva> reservas = null;
    		if (estado == null)
                reservas = OfertaServiceFactory.getService().findReservas(ofertaId, null);
    		else
    			reservas = OfertaServiceFactory.getService().findReservas(ofertaId, Reserva.Estado.valueOf(estado.name()));
            return ReservaToReservaDtoConversor.toReservaDtos(reservas);
    	}
        catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(
                    ex.getInstanceId(), ex.getInstanceType()));
        }
    }
        
            
    @WebMethod(
        operationName="findReserva"
    )
    public ReservaDto findReserva(@WebParam(name="reservaId") Long reservaId)
            throws SoapInstanceNotFoundException{

        try {
            Reserva reserva = OfertaServiceFactory.getService().findReserva(reservaId);
            return ReservaToReservaDtoConversor.toReservaDto(reserva);
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        }
    }

    @WebMethod(
            operationName="reclamarOferta"
        )
        public void reclamarOferta(@WebParam(name="reservaId")  Long reservaId)
                throws SoapInstanceNotFoundException, SoapInputValidationException, SoapOfertaReclamaDateException, SoapReservaEstadoException {
            try {
                OfertaServiceFactory.getService().reclamarOferta(reservaId);
                
            } catch (InstanceNotFoundException ex) {
                throw new SoapInstanceNotFoundException(
                        new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                            ex.getInstanceType()));
            }
            catch (ReservaEstadoException ex) {
                throw new SoapReservaEstadoException(
                        new SoapReservaEstadoExceptionInfo(reservaId, ex.getEstado()));
            }
            catch (OfertaReclamaDateException ex) {
                throw new SoapOfertaReclamaDateException(
                        new SoapOfertaReclamaDateExceptionInfo(ex.getOfertaId()));
            }
        }
    
}
