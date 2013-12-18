package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.model.oferta.Oferta;

public class OfertaToOfertaDtoConversor {

    public static List<OfertaDto> toOfertaDtos(List<Oferta> ofertas) {
        List<OfertaDto> ofertaDtos = new ArrayList<>(ofertas.size());
        for (int i = 0; i < ofertas.size(); i++) {
            Oferta oferta = ofertas.get(i);
            ofertaDtos.add(toOfertaDto(oferta));
        }
        return ofertaDtos;
    }

    public static OfertaDto toOfertaDto(Oferta oferta) {
        return new OfertaDto(oferta.getOfertaId(), oferta.getTitulo(), oferta.getDescripcion(), oferta.getIniReserva(), 
        		oferta.getLimReserva(), oferta.getLimOferta(), oferta.getPrecioReal(), 
        		oferta.getPrecioRebajado(), oferta.getMaxPersonas());
    }

    // ==> Este método solo se llama al añadir una nueva oferta ==>
    public static Oferta toOferta(OfertaDto oferta) {
        return new Oferta(oferta.getOfertaId(), oferta.getTitulo(), oferta.getDescripcion(), oferta.getIniReserva(), 
        		oferta.getLimReserva(), oferta.getLimOferta(), oferta.getPrecioReal(), 
        		oferta.getPrecioRebajado(), oferta.getMaxPersonas(), Oferta.Estado.CREADA, 0L, 0L); 
    }    
    
}
