package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.dto.OfertaDto;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class OfertaDtoToSoapOfertaDtoConversor {
    
    public static es.udc.ws.app.client.service.soap.wsdl.OfertaDto 
            toSoapOfertaDto(OfertaDto oferta) {
        es.udc.ws.app.client.service.soap.wsdl.OfertaDto soapOfertaDto = 
                new es.udc.ws.app.client.service.soap.wsdl.OfertaDto();
        soapOfertaDto.setOfertaId(oferta.getOfertaId());
        soapOfertaDto.setTitulo(oferta.getTitulo());
        soapOfertaDto.setDescripcion(oferta.getDescripcion());
        
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(oferta.getIniReserva().getTime());
        
        GregorianCalendar c2 = new GregorianCalendar();
        c2.setTime(oferta.getLimReserva().getTime());     

        GregorianCalendar c3 = new GregorianCalendar();
        c3.setTime(oferta.getLimOferta().getTime());
        
        try {
			soapOfertaDto.setIniReserva(DatatypeFactory.newInstance().newXMLGregorianCalendar(c1));
	        soapOfertaDto.setIniReserva(DatatypeFactory.newInstance().newXMLGregorianCalendar(c2));
	        soapOfertaDto.setIniReserva(DatatypeFactory.newInstance().newXMLGregorianCalendar(c3));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        soapOfertaDto.setPrecioReal(oferta.getPrecioReal());
        soapOfertaDto.setPrecioReal(oferta.getPrecioRebajado());
        soapOfertaDto.setMaxPersonas(oferta.getMaxPersonas());
        return soapOfertaDto;
    }    
    
    public static OfertaDto toOfertaDto(
            es.udc.ws.app.client.service.soap.wsdl.OfertaDto oferta) {
        return new OfertaDto(oferta.getOfertaId(), oferta.getTitulo(), oferta.getDescripcion(), oferta.getIniReserva(), 
        		oferta.getLimReserva(), oferta.getLimOferta(), oferta.getPrecioReal(), oferta.getPrecioRebajado(),
                oferta.getMaxPersonas());
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
