package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.ClientOfertaService;
import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.app.xml.ParsingException;
import es.udc.ws.app.xml.XmlExceptionConversor;
import es.udc.ws.app.xml.XmlOfertaDtoConversor;
import es.udc.ws.app.xml.XmlReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class RestClientOfertaService implements ClientOfertaService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "RestClientOfertaService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addOferta(OfertaDto oferta) throws InputValidationException {

        PostMethod method =
                new PostMethod(getEndpointAddress() + "ofertas");
        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            Document document;
            try {
                document = XmlOfertaDtoConversor.toXml(oferta);
                XMLOutputter outputter = new XMLOutputter(
                        Format.getPrettyFormat());
                outputter.output(document, xmlOutputStream);
            } catch (IOException ex) {
                throw new InputValidationException(ex.getMessage());
            }
            ByteArrayInputStream xmlInputStream =
                    new ByteArrayInputStream(xmlOutputStream.toByteArray());
            InputStreamRequestEntity requestEntity =
                    new InputStreamRequestEntity(xmlInputStream,
                    "application/xml");
            HttpClient client = new HttpClient();
            method.setRequestEntity(requestEntity);

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_CREATED, method);
            } catch (InputValidationException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return getIdFromHeaders(method);

        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void updateOferta(OfertaDto oferta)
            throws InputValidationException, InstanceNotFoundException {
        PutMethod method =
                new PutMethod(getEndpointAddress() + "ofertas/"
                + oferta.getOfertaId());
        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            Document document;
            try {
                document = XmlOfertaDtoConversor.toXml(oferta);
                XMLOutputter outputter = new XMLOutputter(
                        Format.getPrettyFormat());
                outputter.output(document, xmlOutputStream);
            } catch (IOException ex) {
                throw new InputValidationException(ex.getMessage());
            }
            ByteArrayInputStream xmlInputStream =
                    new ByteArrayInputStream(xmlOutputStream.toByteArray());
            InputStreamRequestEntity requestEntity =
                    new InputStreamRequestEntity(xmlInputStream,
                    "application/xml");
            HttpClient client = new HttpClient();
            method.setRequestEntity(requestEntity);

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_NO_CONTENT, method);
            } catch (InputValidationException | InstanceNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void removeOferta(Long ofertaId) throws InstanceNotFoundException {
        DeleteMethod method =
                new DeleteMethod(getEndpointAddress() + "ofertas/" + ofertaId);
        try {
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(method);
            validateResponse(statusCode, HttpStatus.SC_NO_CONTENT, method);
        } catch (InstanceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public List<OfertaDto> findOfertas(String keywords) {
        GetMethod method = null;
        try {
            method = new GetMethod(getEndpointAddress() + "ofertas/?keywords="
                    + URLEncoder.encode(keywords, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            HttpClient client = new HttpClient();
            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_OK, method);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                return XmlOfertaDtoConversor.toOfertas(
                        method.getResponseBodyAsStream());
            } catch (ParsingException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
    }
    
	@Override
	public OfertaDto findOferta(Long ofertaId) throws InstanceNotFoundException {
	    GetMethod method = null;
	    method = new GetMethod(getEndpointAddress() + "ofertas/" + ofertaId);
	    try {
	        HttpClient client = new HttpClient();
	        int statusCode;
	        try {
	            statusCode = client.executeMethod(method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            validateResponse(statusCode, HttpStatus.SC_OK, method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            return XmlOfertaDtoConversor.toOferta(
	                    method.getResponseBodyAsStream());
	        } catch (ParsingException | IOException ex) {
	            throw new RuntimeException(ex);
	        }
	    } finally {
	        method.releaseConnection();
	    }
	}

    @Override
    public Long reservarOferta(Long ofertaId, String emailUsuario, String numeroTarjeta)
            throws InstanceNotFoundException, InputValidationException {
        PostMethod method = new PostMethod(getEndpointAddress() + "reservas");
        try {
            method.addParameter("ofertaId", Long.toString(ofertaId));
            method.addParameter("emailUsuario", emailUsuario);
            method.addParameter("numeroTarjeta", numeroTarjeta);

            HttpClient client = new HttpClient();

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_CREATED, method);
            } catch (InputValidationException | InstanceNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return getIdFromHeaders(method);
        } finally {
            method.releaseConnection();
        }
    }


	@Override
	public List<ReservaDto> findReservas(Long ofertaId, Short estado) {
	    GetMethod method = null;
	    method = new GetMethod(getEndpointAddress() + "reservas/" + ofertaId + "&estado=" + estado);
	    try {
	        HttpClient client = new HttpClient();
	        int statusCode;
	        try {
	            statusCode = client.executeMethod(method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            validateResponse(statusCode, HttpStatus.SC_OK, method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            return XmlReservaDtoConversor.toReservas(
	                    method.getResponseBodyAsStream());
	        } catch (ParsingException | IOException ex) {
	            throw new RuntimeException(ex);
	        }
	    } finally {
	        method.releaseConnection();
	    }
	}
	

	@Override
	public ReservaDto findReserva(Long reservaId) {
	    GetMethod method = null;
	    method = new GetMethod(getEndpointAddress() + "reservas/");
	    try {
	        HttpClient client = new HttpClient();
	        int statusCode;
	        try {
	            statusCode = client.executeMethod(method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            validateResponse(statusCode, HttpStatus.SC_OK, method);
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            return XmlReservaDtoConversor.toReserva(
	                    method.getResponseBodyAsStream());
	        } catch (ParsingException | IOException ex) {
	            throw new RuntimeException(ex);
	        }
	    } finally {
	        method.releaseConnection();
	    }
	}
	
	@Override
	public boolean reclamarOferta(Long reservaId)
        throws InstanceNotFoundException {
	    PostMethod method = new PostMethod(getEndpointAddress() + "reservas");
	    try {
	        method.addParameter("reservaId", Long.toString(reservaId));
	
	        HttpClient client = new HttpClient();
	
	        int statusCode;
	        try {
	            statusCode = client.executeMethod(method);
	        } catch (IOException ex) {
	            throw new RuntimeException(ex);
	        }
	        try {
	            validateResponse(statusCode, HttpStatus.SC_CREATED, method);
	        } catch (InstanceNotFoundException ex) {
	            throw ex;
	
	
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        return true;
	    } finally {
	        method.releaseConnection();
	    }
	}


    private synchronized String getEndpointAddress() {

        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(
                    ENDPOINT_ADDRESS_PARAMETER);
        }

        return endpointAddress;
    }

    private void validateResponse(int statusCode,
                                  int expectedStatusCode,
                                  HttpMethod method)
            throws InstanceNotFoundException, InputValidationException, OfertaEstadoException, 
            OfertaEmailException, OfertaMaxPersonasException, OfertaReservaDateException, OfertaReclamaDateException,
            ParsingException {

        InputStream in;
        try {
            in = method.getResponseBodyAsStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String contentType = getResponseHeader(method, "Content-Type");
        boolean isXmlResponse = "application/xml"
                .equalsIgnoreCase(contentType);
        if (!isXmlResponse && statusCode >= 400) {
            throw new RuntimeException("HTTP error; status code = "
                    + statusCode);
        }
        switch (statusCode) {
            case HttpStatus.SC_NOT_FOUND:
                try {
                    throw XmlExceptionConversor
                            .fromInstanceNotFoundExceptionXml(in);
                } catch (ParsingException e) {
                    throw new RuntimeException(e);
                }
            case HttpStatus.SC_BAD_REQUEST:
                try {
                    throw XmlExceptionConversor
                            .fromInputValidationExceptionXml(in);
                } catch (ParsingException e) {
                    throw new RuntimeException(e);
                }
            case HttpStatus.SC_GONE:
                try {
                    throw XmlExceptionConversor
                            .fromOfertaReservaDateExceptionXml(in);
                } catch (ParsingException e) {
                    throw new RuntimeException(e);
                }
            default:
                if (statusCode != expectedStatusCode) {
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
                }
                break;
        }
    }

    private static Long getIdFromHeaders(HttpMethod method) {
        String location = getResponseHeader(method, "Location");
        if (location != null) {
            int idx = location.lastIndexOf('/');
            return Long.valueOf(location.substring(idx + 1));
        }
        return null;
    }

    private static String getResponseHeader(HttpMethod method,
            String headerName) {
        Header[] headers = method.getResponseHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            if (headerName.equalsIgnoreCase(header.getName())) {
                return header.getValue();
            }
        }
        return null;
    }
}
