package es.udc.ws.app.client.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientOfertaService {

    public Long addOferta(OfertaDto movie)
            throws InputValidationException;

    public void updateOferta(OfertaDto movie)
            throws InputValidationException, InstanceNotFoundException;

    public void removeOferta(Long movieId) throws InstanceNotFoundException;
    
    public OfertaDto findOferta(Long ofertaId) throws InstanceNotFoundException;

    public List<OfertaDto> findOfertas(String keywords, Short estado, Calendar fecha);

    //public List<OfertaDto> findOfertas();
    
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException;

    public List<ReservaDto> findReservas(Long ofertaId, Short estado) throws InstanceNotFoundException;

    public ReservaDto findReserva(Long reservaId) throws InstanceNotFoundException;//,ReservaExpirationException;

    //reclamarOferta: Si devuelve false ==> la reserva está cerrada. Si devuelve true ==> la reserva está abierta.
    public boolean reclamarOferta(Long reservaId) throws InstanceNotFoundException;
    

}
