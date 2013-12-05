package es.udc.ws.app.soapservice;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
//import es.udc.ws.app.exceptions.ReservaExpirationException;
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
            throws SoapInputValidationException, SoapInstanceNotFoundException, OfertaEstadoException {
        Oferta oferta = OfertaToOfertaDtoConversor.toOferta(ofertaDto);
        try {
            OfertaServiceFactory.getService().updateOferta(oferta);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        }
    }

    @WebMethod(
        operationName="removeOferta"
    )
    public void removeOferta(@WebParam(name="ofertaId") Long ofertaId)
            throws SoapInstanceNotFoundException, InputValidationException, OfertaEstadoException {
        try {
            OfertaServiceFactory.getService().removeOferta(ofertaId);
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(
                    ex.getInstanceId(), ex.getInstanceType()));
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
            @WebParam(name="keywords") String keywords,
            @WebParam(name="estado") short estado,
            @WebParam(name="fecha") Calendar fecha) {
        List<Oferta> ofertas =
                OfertaServiceFactory.getService().findOfertas(keywords, estado, fecha);
        return OfertaToOfertaDtoConversor.toOfertaDtos(ofertas);
    }

    @WebMethod(
        operationName="reservarOferta"
    )
    public Long reservarOferta(@WebParam(name="ofertaId")  Long ofertaId,
                         @WebParam(name="emailUsuario")   String emailUsuario,
                         @WebParam(name="numeroTarjeta") String numeroTarjeta)
            throws SoapInstanceNotFoundException, SoapInputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException {
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
    }

    @WebMethod(
            operationName="findReservas"
        )
        public List<ReservaDto> findReservas(
                @WebParam(name="ofertaId") Long ofertaId,
                @WebParam(name="estado") short estado) throws InstanceNotFoundException {
            List<Reserva> reservas = OfertaServiceFactory.getService().findReservas(ofertaId, estado);
            return ReservaToReservaDtoConversor.toReservaDtos(reservas);
    }
        
            
    @WebMethod(
        operationName="findReserva"
    )
    public ReservaDto findReserva(@WebParam(name="reservaId") Long reservaId)
            throws SoapInstanceNotFoundException,
            SoapReservaExpirationException {

        try {
            Reserva reserva = OfertaServiceFactory.getService().findReserva(reservaId);
            return ReservaToReservaDtoConversor.toReservaDto(reserva);
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        } /*catch (ReservaExpirationException ex) {
            throw new SoapReservaExpirationException(
                    new SoapReservaExpirationExceptionInfo(ex.getReservaId(),
                        ex.getExpirationDate()));
        }*/
    }

    @WebMethod(
            operationName="reclamarOferta"
        )
        public boolean reclamarOferta(@WebParam(name="reservaId")  Long reservaId)
                throws SoapInstanceNotFoundException, SoapInputValidationException, OfertaReclamaDateException {
            try {
            	
                return OfertaServiceFactory.getService().reclamarOferta(reservaId);
                
            } catch (InstanceNotFoundException ex) {
                throw new SoapInstanceNotFoundException(
                        new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                            ex.getInstanceType()));
            }
        }
    
}
