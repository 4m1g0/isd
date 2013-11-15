package es.udc.ws.app.model.ofertaservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface OfertaService {

    public Oferta addOferta(Oferta oferta) throws InputValidationException;

    public void updateOferta(Oferta oferta) throws InputValidationException,
            InstanceNotFoundException;

    public void removeOferta(Long ofertaId) throws InstanceNotFoundException, InputValidationException;

    public Oferta findOferta(Long ofertaId) throws InstanceNotFoundException;
    
    public List<Oferta> findOfertas(String keywords, Short estado, Calendar fecha);
    
    public List<Oferta> findOfertas(Object param1, Object param2);

    public List<Oferta> findOfertas(Object param1);
    
    public List<Oferta> findOfertas();

    /*public Sale buyMovie(Long movieId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException;

    public Sale findSale(Long saleId) throws InstanceNotFoundException,
            SaleExpirationException;*/
}
