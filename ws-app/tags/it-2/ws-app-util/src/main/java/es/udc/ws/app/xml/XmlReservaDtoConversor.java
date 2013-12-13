package es.udc.ws.app.xml;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
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

public class XmlReservaDtoConversor {

    public final static Namespace XML_NS = Namespace
            .getNamespace("http://ws.udc.es/reservas/xml");

    public static Document toResponse(ReservaDto reserva)
            throws IOException {

        Element reservaElement = toXml(reserva);

        return new Document(reservaElement);
    }

    public static ReservaDto toReserva(InputStream reservaXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(reservaXml);
            Element rootElement = document.getRootElement();

            return toReserva(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Element toXml(ReservaDto reserva) {

        Element reservaElement = new Element("reserva", XML_NS);

        if (reserva.getReservaId() != null) {
            Element reservaIdElement = new Element("reservaId", XML_NS);
            reservaIdElement.setText(reserva.getReservaId().toString());
            reservaElement.addContent(reservaIdElement);
        }

        if (reserva.getOfertaId() != null) {
            Element ofertaIdElement = new Element("ofertaId", XML_NS);
            ofertaIdElement.setText(reserva.getOfertaId().toString());
            reservaElement.addContent(ofertaIdElement);
        }
        
        Element estadoElement = new Element("estado", XML_NS);
        estadoElement.setText(Integer.toString(reserva.getEstado()));
        reservaElement.addContent(estadoElement);
        
        if (reserva.getFechaReserva() != null) {
            Element fechaReservaElement = getFecha(reserva
                    .getFechaReserva());
            reservaElement.addContent(fechaReservaElement);
        }

        return reservaElement;
    }

    private static ReservaDto toReserva(Element reservaElement)
            throws ParsingException, DataConversionException,
            NumberFormatException {
        if (!"reserva".equals(reservaElement.getName())) {
            throw new ParsingException("Unrecognized element '"
                    + reservaElement.getName() + "' ('reserva' expected)");
        }
        Element reservaIdElement = reservaElement.getChild("reservaId", XML_NS);
        Long reservaId = null;
        if (reservaIdElement != null) {
            reservaId = Long.valueOf(reservaIdElement.getTextTrim());
        }

        Element ofertaIdElement = reservaElement.getChild("ofertaId", XML_NS);
        Long ofertaId = null;
        if (ofertaIdElement != null) {
            ofertaId = Long.valueOf(ofertaIdElement.getTextTrim());
        }

        short estado = Short.valueOf(reservaElement.getChildTextTrim("estado", XML_NS));
        
        Calendar fechaReserva = getFecha(reservaElement.getChild(
                "fechaReserva", XML_NS));

        String ofertaUrl = reservaElement.getChildTextTrim("ofertaUrl", XML_NS);

        return new ReservaDto(reservaId, ofertaId, estado, fechaReserva);
    }

    public static List<ReservaDto> toReservas(InputStream reservaXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(reservaXml);
            Element rootElement = document.getRootElement();

            if(!"reservas".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                    + rootElement.getName() + "' ('reservas' expected)");
            }
            @SuppressWarnings("unchecked")
			List<Element> children = rootElement.getChildren();
            List<ReservaDto> reservaDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                reservaDtos.add(toReserva(element));
            }

            return reservaDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
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

    private static Element getFecha(Calendar fecha) {

        Element releaseDateElement = new Element("fecha", XML_NS);
        int day = fecha.get(Calendar.DAY_OF_MONTH);
        int month = fecha.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = fecha.get(Calendar.YEAR);

        releaseDateElement.setAttribute("day", Integer.toString(day));
        releaseDateElement.setAttribute("month", Integer.toString(month));
        releaseDateElement.setAttribute("year", Integer.toString(year));

        return releaseDateElement;

    }

}
