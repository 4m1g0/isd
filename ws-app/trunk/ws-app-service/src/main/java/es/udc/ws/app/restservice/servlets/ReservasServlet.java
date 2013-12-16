package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.ofertaservice.OfertaServiceFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.serviceutil.ReservaToReservaDtoConversor;
import es.udc.ws.app.xml.XmlExceptionConversor;
import es.udc.ws.app.xml.XmlReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class ReservasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String ofertaIdParameter = req.getParameter("ofertaId");
        if (ofertaIdParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " +
                        "parameter 'ofertaId' is mandatory")), null);
            return;
        }
        Long ofertaId;
        try {
            ofertaId = Long.valueOf(ofertaIdParameter);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " +
                        "parameter 'ofertaId' is invalid '" +
                        ofertaIdParameter + "'")),
                    null);

            return;
        }
        String emailUsuario = req.getParameter("emailUsuario");
        if (emailUsuario == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " +
                        "parameter 'emailUsuario' is mandatory")), null);
            return;
        }
        String numeroTarjeta = req.getParameter("numeroTarjeta");
        if (numeroTarjeta == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: "+
                        "parameter 'numeroTarjeta' is mandatory")), null);

            return;
        }
        Oferta oferta = null;
        Reserva reserva = null;
        try {
            oferta = OfertaServiceFactory.getService().findOferta(ofertaId);
            reserva = OfertaServiceFactory.getService().findReserva(OfertaServiceFactory.getService()
                    .reservarOferta(ofertaId, emailUsuario, numeroTarjeta));
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlExceptionConversor.toInstanceNotFoundException(
                    new InstanceNotFoundException(ex.getInstanceId()
                        .toString(),ex.getInstanceType())), null);
            return;
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException(ex.getMessage())), null);
            return;
        } catch (OfertaMaxPersonasException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE, 
                    XmlExceptionConversor.toOfertaMaxPersonasException(
                    new OfertaMaxPersonasException(ofertaId, oferta.getMaxPersonas())),
                    null);
            return;
		} catch (OfertaEmailException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toOfertaEmailException(
                    new OfertaEmailException(ofertaId, emailUsuario)), null);
            return;
		} catch (OfertaReservaDateException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE, 
                    XmlExceptionConversor.toOfertaReservaDateException(
                    new OfertaReservaDateException(ofertaId)), null);
            return;	
		}
        ReservaDto reservaDto = ReservaToReservaDtoConversor.toReservaDto(reserva);

        String reservaURL = req.getRequestURL().append("/").append(
                reserva.getReservaId()).toString();

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", reservaURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlReservaDtoConversor.toResponse(reservaDto), headers);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.length() == 0 || "/".equals(path)) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " +
                            "unable to find reserva id")), null);
            return;
        }
        String reservaIdAsString = path.endsWith("/") && path.length() > 2 ?
                    path.substring(1, path.length() - 1) : path.substring(1);
        Long reservaId;
        try {
            reservaId = Long.valueOf(reservaIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " +
                        "reserva id is not valid '" + reservaIdAsString)), null);
            return;
        }
        Reserva reserva;
        try {
            reserva = OfertaServiceFactory.getService().findReserva(reservaId);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlExceptionConversor.toInstanceNotFoundException(
                    new InstanceNotFoundException(ex.getInstanceId()
                        .toString(),ex.getInstanceType())), null);
           return;
        }

        ReservaDto reservaDto = ReservaToReservaDtoConversor.toReservaDto(reserva);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlReservaDtoConversor.toResponse(reservaDto), null);

    }
}
