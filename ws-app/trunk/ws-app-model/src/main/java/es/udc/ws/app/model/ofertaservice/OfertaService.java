package es.udc.ws.app.model.ofertaservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface OfertaService {

    public Oferta addOferta(Oferta oferta) throws InputValidationException;

    public void updateOferta(Oferta oferta) throws InputValidationException,
            InstanceNotFoundException, OfertaEstadoException;

    public void removeOferta(Long ofertaId) throws InstanceNotFoundException, InputValidationException, OfertaEstadoException;

    public Oferta findOferta(Long ofertaId) throws InstanceNotFoundException;
    
    public List<Oferta> findOfertas(String keywords, Short estado, Calendar fecha);
    
    public List<Oferta> findOfertas();

    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException;

    public List<Reserva> findReservas(Long ofertaId, Short estado) throws InstanceNotFoundException;

    public Reserva findReserva(Long reservaId) throws InstanceNotFoundException;//,ReservaExpirationException;
    
    //reclamarOferta: Si devuelve false ==> la reserva está cerrada. Si devuelve true ==> la reserva está abierta.
    public boolean reclamarOferta(Long reservaId) throws InstanceNotFoundException, OfertaReclamaDateException;
}
