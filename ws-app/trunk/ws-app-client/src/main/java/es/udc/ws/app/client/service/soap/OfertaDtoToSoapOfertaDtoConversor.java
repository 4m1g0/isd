package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.dto.OfertaDto;
import java.util.ArrayList;
import java.util.List;

public class OfertaDtoToSoapOfertaDtoConversor {
    
    public static es.udc.ws.app.client.service.soap.wsdl.OfertaDto 
            toSoapOfertaDto(OfertaDto oferta) {
        es.udc.ws.app.client.service.soap.wsdl.OfertaDto soapOfertaDto = 
                new es.udc.ws.app.client.service.soap.wsdl.OfertaDto();
        soapOfertaDto.setOfertaId(oferta.getOfertaId());
        soapOfertaDto.setTitulo(oferta.getTitulo());
        soapOfertaDto.setDescripcion(oferta.getDescripcion());
        soapOfertaDto.setIniReserva(oferta.getIniReserva());
        soapOfertaDto.setLimReserva(oferta.getLimReserva());
        soapOfertaDto.setLimOferta(oferta.getLimOferta());
        soapOfertaDto.setPrecioReal(oferta.getPrecioReal());
        soapOfertaDto.setPrecioReal(oferta.getPrecioRebajado());
        soapOfertaDto.setMaxPersonas(oferta.getMaxPersonas());
        return soapOfertaDto;
    }    
    
    public static OfertaDto toOfertaDto(
            es.udc.ws.app.client.service.soap.wsdl.OfertaDto oferta) {
        return new OfertaDto(oferta.getOfertaId(), oferta.getTitulo(), 
                oferta.getMaxPersonas(), oferta.getDescripcion(), oferta.getPrecioReal());
    }     
    
    public static List<OfertaDto> toOfertaDtos(
            List<es.udc.ws.app.client.service.soap.wsdl.OfertaDto> ofertas) {
        List<OfertaDto> ofertaDtos = new ArrayList<>(ofertas.size());
        for (int i = 0; i < ofertas.size(); i++) {
            es.udc.ws.app.client.service.soap.wsdl.OfertaDto oferta = 
                    ofertas.get(i);
            ofertaDtos.add(toOfertaDto(oferta));
            
        }
        return ofertaDtos;
    }    
    
}
