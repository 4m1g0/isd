package es.udc.ws.app.client.service;

import java.util.List;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientOfertaService {

    public Long addOferta(OfertaDto oferta)
            throws InputValidationException;

    public void updateOferta(OfertaDto oferta)
            throws InputValidationException, InstanceNotFoundException, OfertaEstadoException;

    public void removeOferta(Long ofertaId) throws InstanceNotFoundException, InputValidationException, OfertaEstadoException;
    
    public OfertaDto findOferta(Long ofertaId) throws InstanceNotFoundException;

    public List<OfertaDto> findOfertas(String keywords);

    //public List<OfertaDto> findOfertas();
    
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException, OfertaMaxPersonasException, OfertaEmailException, OfertaReservaDateException;

    public List<ReservaDto> findReservas(Long ofertaId, Short estado) throws InstanceNotFoundException;

    public ReservaDto findReserva(Long reservaId) throws InstanceNotFoundException;

    //reclamarOferta: Si devuelve false ==> la reserva está cerrada. Si devuelve true ==> la reserva está abierta.
    public boolean reclamarOferta(Long reservaId) throws InstanceNotFoundException, OfertaReclamaDateException, OfertaReservaDateException;
    

}
