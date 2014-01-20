package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.exceptions.ReservaEstadoException;
import es.udc.ws.app.model.oferta.Oferta;
import es.udc.ws.app.model.ofertaservice.OfertaServiceFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.Reserva.Estado;
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
        String reservaIdParameter = req.getParameter("reservaId");

        if (ofertaIdParameter == null && reservaIdParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("No parameters")), null);
            return;
        }
        
        // ReservarOferta
        else if (reservaIdParameter == null) {
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
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
	                    XmlExceptionConversor.toOfertaMaxPersonasException(
	                    new OfertaMaxPersonasException(ofertaId, oferta.getMaxPersonas())),
	                    null);
	            return;
			} catch (OfertaEmailException e) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
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
	                XmlReservaDtoConversor.toXml(reservaDto), headers);
        }
        
        // ReclamarOferta
        else {
            Long reservaId;
            try {
    	            reservaId = Long.valueOf(reservaIdParameter);
    	        } catch (NumberFormatException ex) {
    	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
    	                    XmlExceptionConversor.toInputValidationExceptionXml(
    	                    new InputValidationException("Invalid Request: " +
    	                        "parameter 'reservaId' is invalid '" +
    	                        reservaIdParameter + "'")),
    	                    null);
    	            return;
    	        }
	        Reserva reserva = null;
	        try {
	            reserva = OfertaServiceFactory.getService().findReserva(reservaId);
        		OfertaServiceFactory.getService().reclamarOferta(reservaId);

	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlExceptionConversor.toInstanceNotFoundException(
	                    new InstanceNotFoundException(ex.getInstanceId()
	                        .toString(),ex.getInstanceType())), null);
	            return;
			}
	        catch (ReservaEstadoException e) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
	                    XmlExceptionConversor.toReservaEstadoException(
	                    new ReservaEstadoException(reservaId, e.getEstado())), null);
	            return;	
	        } catch (OfertaReclamaDateException e) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE, 
	                    XmlExceptionConversor.toOfertaReclamaDateException(
	                    new OfertaReclamaDateException(reserva.getOfertaId())), null);
	            return;	
			}
	        ReservaDto reservaDto = ReservaToReservaDtoConversor.toReservaDto(reserva);
	
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                XmlReservaDtoConversor.toXml(reservaDto), null);
        }
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
        
        // FindReservas(ofertaId, estado)
        else if (path.indexOf("&estado=") != -1) {
        	String ofertaIdAsString = path.substring(0, path.lastIndexOf("&estado=")).length() > 1 
        			? path.substring(1, path.lastIndexOf("&estado=")) : path.substring(1, path.lastIndexOf("&estado="))+"/";
            Long ofertaId;
	        try {
	            ofertaId = Long.valueOf(ofertaIdAsString);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: " +
	                        "oferta id is not valid " + ofertaIdAsString)), null);
	            return;
	        }
	        
	        List<Reserva> reservas;
	        // estado = null
	        if (path.endsWith("&estado=")) {
		        // Obtención de las reservas
		        try {
		            reservas = OfertaServiceFactory.getService().findReservas(ofertaId, null);
		        } catch (InstanceNotFoundException ex) {
		            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
		                    XmlExceptionConversor.toInstanceNotFoundException(
		                    new InstanceNotFoundException(ex.getInstanceId()
		                        .toString(),ex.getInstanceType())), null);
		           return;
		        }
	        }
		    
	        // estado != null
	        else {
	        	System.out.println(path);
	            String estadoAsString = path.substring(path.lastIndexOf("&estado=")+"&estado=".length(), path.length());
	            Integer intEstado;
	            Estado estado = null;
		        try {
		            intEstado = Integer.valueOf(estadoAsString);
		        } catch (NumberFormatException ex) {
		            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
		                    XmlExceptionConversor.toInputValidationExceptionXml(
		                    new InputValidationException("Invalid Request: " +
		                        "estado is not valid " + estadoAsString)), null);
		            return;
		        }
		        if (intEstado == 0)
		        	estado = Estado.PENDIENTE;
		        if (intEstado == 1)
		        	estado = Estado.CERRADA;
		        	
		        // Obtención de las reservas
		        try {
		            reservas = OfertaServiceFactory.getService().findReservas(ofertaId, estado);
		        } catch (InstanceNotFoundException ex) {
		            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
		                    XmlExceptionConversor.toInstanceNotFoundException(
		                    new InstanceNotFoundException(ex.getInstanceId()
		                        .toString(),ex.getInstanceType())), null);
		          return;
		        }
	        }
	
	        // Respuesta
	        List<ReservaDto> reservaDtos = ReservaToReservaDtoConversor.toReservaDtos(reservas);
	
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                XmlReservaDtoConversor.toXml(reservaDtos), null);
        }
	        	
        // FindReserva(reservaId)
        else {
	        String reservaIdAsString = path.endsWith("/") && path.length() > 2 ?
	                    path.substring(1, path.length() - 1) : path.substring(1);
	        Long reservaId;
	        try {
	            reservaId = Long.valueOf(reservaIdAsString);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: " +
	                        "reserva id is not valid " + reservaIdAsString)), null);
	            return;
	        }
	        
	        // Obtención de la reserva
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
	
	        // Respuesta
	        ReservaDto reservaDto = ReservaToReservaDtoConversor.toReservaDto(reserva);
	
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                XmlReservaDtoConversor.toXml(reservaDto), null);

        }
    }
}
