package es.udc.ws.app.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.OfertaDto;

public class XmlOfertaDtoConversor {

    public final static Namespace XML_NS =
            Namespace.getNamespace("http://ws.udc.es/ofertas/xml");

    public static Document toXml(OfertaDto oferta)
            throws IOException {

        Element ofertaElement = toJDOMElement(oferta);

        return new Document(ofertaElement);
    }

    public static Document toXml(List<OfertaDto> oferta)
            throws IOException {

        Element ofertasElement = new Element("ofertas", XML_NS);
        for (int i = 0; i < oferta.size(); i++) {
            OfertaDto xmlOfertaDto = oferta.get(i);
            Element ofertaElement = toJDOMElement(xmlOfertaDto);
            ofertasElement.addContent(ofertaElement);
        }

        return new Document(ofertasElement);
    }

    public static OfertaDto toOferta(InputStream ofertaXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ofertaXml);
            Element rootElement = document.getRootElement();

            return toOferta(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<OfertaDto> toOfertas(InputStream ofertaXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ofertaXml);
            Element rootElement = document.getRootElement();

            if(!"ofertas".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                    + rootElement.getName() + "' ('ofertas' expected)");
            }
            @SuppressWarnings("unchecked")
			List<Element> children = rootElement.getChildren();
            List<OfertaDto> ofertaDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                ofertaDtos.add(toOferta(element));
            }

            return ofertaDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Element toJDOMElement(OfertaDto oferta) {

    	Element ofertaElement = new Element("oferta", XML_NS);

        if (oferta.getOfertaId() != null) {
            Element identifierElement = new Element("ofertaId", XML_NS);
            identifierElement.setText(oferta.getOfertaId().toString());
            ofertaElement.addContent(identifierElement);
        }

        Element tituloElement = new Element("titulo", XML_NS);
        tituloElement.setText(oferta.getTitulo());
        ofertaElement.addContent(tituloElement);
        
        Element descripcionElement = new Element("descripcion", XML_NS);
        descripcionElement.setText(oferta.getDescripcion());
        ofertaElement.addContent(descripcionElement);

        if (oferta.getIniReserva() != null) {
            Element iniReservaElement = getFecha(oferta
                    .getIniReserva(), "iniReserva");
            ofertaElement.addContent(iniReservaElement);
        }
        
        if (oferta.getLimReserva() != null) {
            Element limReservaElement = getFecha(oferta
                    .getLimReserva(), "limReserva");
            ofertaElement.addContent(limReservaElement);
        }
        
        if (oferta.getLimOferta() != null) {
            Element limOfertaElement = getFecha(oferta
                    .getLimOferta(), "limOferta");
            ofertaElement.addContent(limOfertaElement);
        }
        
        Element precioRealElement = new Element("precioReal", XML_NS);
        precioRealElement.setText(Double.toString(oferta.getPrecioReal()));
        ofertaElement.addContent(precioRealElement);
        
        Element precioRebajadoElement = new Element("precioRebajado", XML_NS);
        precioRebajadoElement.setText(Double.toString(oferta.getPrecioRebajado()));
        ofertaElement.addContent(precioRebajadoElement);

        Element maxPersonasElement = new Element("maxPersonas", XML_NS);
        maxPersonasElement.setText(Long.toString(oferta.getMaxPersonas()));
        ofertaElement.addContent(maxPersonasElement);


        return ofertaElement;
    }

    private static Calendar getFecha(Element fechaElement)
            throws DataConversionException {

        if (fechaElement == null) {
            return null;
        }
        int day = fechaElement.getAttribute("day").getIntValue();
        int month = fechaElement.getAttribute("month").getIntValue();
        int year = fechaElement.getAttribute("year").getIntValue();
        Calendar releaseDate = Calendar.getInstance();

        releaseDate.set(Calendar.DAY_OF_MONTH, day);
        releaseDate.set(Calendar.MONTH, Calendar.JANUARY + month - 1);
        releaseDate.set(Calendar.YEAR, year);

        return releaseDate;

    }

    private static Element getFecha(Calendar fecha, String name) {

        Element releaseDateElement = new Element(name, XML_NS);
        int day = fecha.get(Calendar.DAY_OF_MONTH);
        int month = fecha.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = fecha.get(Calendar.YEAR);

        releaseDateElement.setAttribute("day", Integer.toString(day));
        releaseDateElement.setAttribute("month", Integer.toString(month));
        releaseDateElement.setAttribute("year", Integer.toString(year));

        return releaseDateElement;

    }
    
    private static OfertaDto toOferta(Element ofertaElement)
            throws ParsingException, DataConversionException {
        if (!"oferta".equals(
                ofertaElement.getName())) {
            throw new ParsingException("Unrecognized element '"
                    + ofertaElement.getName() + "' ('oferta' expected)");
        }
        Element identifierElement = ofertaElement.getChild("ofertaId", XML_NS);
        Long identifier = null;

        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        String titulo = ofertaElement.getChildTextNormalize("titulo", XML_NS);

        String descripcion = ofertaElement
                .getChildTextNormalize("descripcion", XML_NS);

        Calendar iniReserva = getFecha(ofertaElement.getChild(
                "iniReserva", XML_NS));
        
        Calendar limReserva = getFecha(ofertaElement.getChild(
                "limReserva", XML_NS));
        
        Calendar limOferta = getFecha(ofertaElement.getChild(
                "limOferta", XML_NS));
        
        float precioReal = Float.valueOf(
                ofertaElement.getChildTextTrim("precioReal", XML_NS));
        
        float precioRebajado = Float.valueOf(
                ofertaElement.getChildTextTrim("precioRebajado", XML_NS));
        
        Long maxPersonas = Long.valueOf(
                ofertaElement.getChildTextTrim("maxPersonas", XML_NS));

        /*short estado = Short.valueOf(
                ofertaElement.getChildTextTrim("estado", XML_NS));*/

        return new OfertaDto(identifier, titulo, descripcion, iniReserva, limReserva, limOferta, 
        		precioReal, precioRebajado, maxPersonas);

    }

}
