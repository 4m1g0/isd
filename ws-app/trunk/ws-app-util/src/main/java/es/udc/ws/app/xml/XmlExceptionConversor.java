package es.udc.ws.app.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.exceptions.OfertaEmailException;
import es.udc.ws.app.exceptions.OfertaEstadoException;
import es.udc.ws.app.exceptions.OfertaMaxPersonasException;
import es.udc.ws.app.exceptions.OfertaReclamaDateException;
import es.udc.ws.app.exceptions.OfertaReservaDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class XmlExceptionConversor {

    public final static String CONVERSION_PATTERN =
            "EEE, d MMM yyyy HH:mm:ss Z";

    public final static Namespace XML_NS = XmlOfertaDtoConversor.XML_NS;

    public static InputValidationException
            fromInputValidationExceptionXml(InputStream ex)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element message = rootElement.getChild("message", XML_NS);

            return new InputValidationException(message.getText());
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static InstanceNotFoundException
            fromInstanceNotFoundExceptionXml(InputStream ex)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element instanceId = rootElement.getChild("instanceId", XML_NS);
            Element instanceType =
                    rootElement.getChild("instanceType", XML_NS);

            return new InstanceNotFoundException(instanceId.getText(),
                    instanceType.getText());
        } catch (JDOMException | IOException e) {
            throw new ParsingException(e);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static OfertaEstadoException fromOfertaEstadoExceptionXml(InputStream ex)
	    throws ParsingException {
			try {
			
			    SAXBuilder builder = new SAXBuilder();
			    Document document = builder.build(ex);
			    Element rootElement = document.getRootElement();
			
			    Element instanceId = rootElement.getChild("ofertaId", XML_NS);
			    Element estadoElement = rootElement.getChild("estado", XML_NS);
			    
			    return new OfertaEstadoException(
			            Long.parseLong(instanceId.getTextTrim()),
			            Short.parseShort(estadoElement.getText()));
			} catch (JDOMException | IOException | NumberFormatException e) {
			    throw new ParsingException(e);
		} catch (Exception e) {
		    throw new ParsingException(e);
		}
	}
    
    public static OfertaEmailException fromOfertaEmailExceptionXml(InputStream ex)
    	    throws ParsingException {
    			try {
    			
    			    SAXBuilder builder = new SAXBuilder();
    			    Document document = builder.build(ex);
    			    Element rootElement = document.getRootElement();
    			
    			    Element instanceId = rootElement.getChild("ofertaId", XML_NS);
    			    Element emailUsuarioElement = rootElement.getChild("emailUsuario", XML_NS);
    			    
    			    return new OfertaEmailException(
    			            Long.parseLong(instanceId.getTextTrim()),
    			            emailUsuarioElement.getText());
    			} catch (JDOMException | IOException | NumberFormatException e) {
    			    throw new ParsingException(e);
    		} catch (Exception e) {
    		    throw new ParsingException(e);
    		}
    	}
    
    public static OfertaMaxPersonasException fromOfertaMaxPersonasExceptionXml(InputStream ex)
    	    throws ParsingException {
    			try {
    			
    			    SAXBuilder builder = new SAXBuilder();
    			    Document document = builder.build(ex);
    			    Element rootElement = document.getRootElement();
    			
    			    Element instanceId = rootElement.getChild("ofertaId", XML_NS);
    			    Element maxPersonasElement = rootElement.getChild("maxPersonas", XML_NS);
    			    
    			    return new OfertaMaxPersonasException(
    			            Long.parseLong(instanceId.getTextTrim()),
    			            Short.parseShort(maxPersonasElement.getText()));
    			} catch (JDOMException | IOException | NumberFormatException e) {
    			    throw new ParsingException(e);
    		} catch (Exception e) {
    		    throw new ParsingException(e);
    		}
    	}
    
    public static OfertaReservaDateException fromOfertaReservaDateExceptionXml(InputStream ex)
    	    throws ParsingException {
    			try {
    			
    			    SAXBuilder builder = new SAXBuilder();
    			    Document document = builder.build(ex);
    			    Element rootElement = document.getRootElement();
    			
    			    Element instanceId = rootElement.getChild("ofertaId", XML_NS);
    			    
    			    return new OfertaReservaDateException(
    			            Long.parseLong(instanceId.getTextTrim()));
    			} catch (JDOMException | IOException | NumberFormatException e) {
    			    throw new ParsingException(e);
    		} catch (Exception e) {
    		    throw new ParsingException(e);
    		}
    	}
    
    public static OfertaReclamaDateException fromOfertaReclamaDateExceptionXml(InputStream ex)
    	    throws ParsingException {
    			try {
    			
    			    SAXBuilder builder = new SAXBuilder();
    			    Document document = builder.build(ex);
    			    Element rootElement = document.getRootElement();
    			
    			    Element instanceId = rootElement.getChild("ofertaId", XML_NS);
    			    
    			    return new OfertaReclamaDateException(
    			            Long.parseLong(instanceId.getTextTrim()));
    			} catch (JDOMException | IOException | NumberFormatException e) {
    			    throw new ParsingException(e);
    		} catch (Exception e) {
    		    throw new ParsingException(e);
    		}
    	}
    
    public static Document toInputValidationExceptionXml(
                InputValidationException ex)
            throws IOException {

        Element exceptionElement =
                new Element("InputValidationException", XML_NS);

        Element messageElement = new Element("message", XML_NS);
        messageElement.setText(ex.getMessage());
        exceptionElement.addContent(messageElement);

        return new Document(exceptionElement);
    }

    public static Document toInstanceNotFoundException (
                InstanceNotFoundException ex)
            throws IOException {

        Element exceptionElement =
                new Element("InstanceNotFoundException", XML_NS);

        if(ex.getInstanceId() != null) {
            Element instanceIdElement = new Element("instanceId", XML_NS);
            instanceIdElement.setText(ex.getInstanceId().toString());

            exceptionElement.addContent(instanceIdElement);
        }

        if(ex.getInstanceType() != null) {
            Element instanceTypeElement = new Element("instanceType", XML_NS);
            instanceTypeElement.setText(ex.getInstanceType());

            exceptionElement.addContent(instanceTypeElement);
        }
        return new Document(exceptionElement);
    }

	public static Document toOfertaEmailException(
			OfertaEmailException ex) 
        throws IOException {

		    Element exceptionElement =
		            new Element("OfertaEmailException", XML_NS);
		
		    if(ex.getOfertaId() != null) {
		        Element ofertaIdElement = new Element("ofertaId", XML_NS);
		        ofertaIdElement.setText(ex.getOfertaId().toString());
		        exceptionElement.addContent(ofertaIdElement);
		    }
		
		    if(ex.getEmailUsuario() != null) {
		
		        Element emailUsuarioElement = new
		                Element("emailUsuario", XML_NS);
		        emailUsuarioElement.setText(ex.getEmailUsuario().toString());
		
		        exceptionElement.addContent(emailUsuarioElement);
		    }
		
		    return new Document(exceptionElement);
	}
	
	public static Document toOfertaMaxPersonasException(
			OfertaMaxPersonasException ex) 
        throws IOException {

		    Element exceptionElement =
		            new Element("OfertaMaxPersonasException", XML_NS);
		
		    if(ex.getOfertaId() != null) {
		        Element ofertaIdElement = new Element("ofertaId", XML_NS);
		        ofertaIdElement.setText(ex.getOfertaId().toString());
		        exceptionElement.addContent(ofertaIdElement);
		    }
		
		    //if(ex.getMaxPersonas() != null) {
		
		        Element maxPersonasElement = new
		                Element("maxPersonas", XML_NS);
		        maxPersonasElement.setText(Short.toString(ex.getMaxPersonas()));
		
		        exceptionElement.addContent(maxPersonasElement);
		    //}
		
		    return new Document(exceptionElement);
	}
	
	public static Document toOfertaEstadoException(
			OfertaEstadoException ex) 
        throws IOException {

		    Element exceptionElement =
		            new Element("OfertaEstadoException", XML_NS);
		
		    if(ex.getOfertaId() != null) {
		        Element ofertaIdElement = new Element("ofertaId", XML_NS);
		        ofertaIdElement.setText(ex.getOfertaId().toString());
		        exceptionElement.addContent(ofertaIdElement);
		    }
		
		    //if(ex.getEstado() != null) {
		
		        Element estadoElement = new
		                Element("estado", XML_NS);
		        estadoElement.setText(Short.toString(ex.getEstado()));
		
		        exceptionElement.addContent(estadoElement);
		    //}
		
		    return new Document(exceptionElement);
	}

	public static Document toOfertaReservaDateException(
			OfertaReservaDateException ex) 
        throws IOException {

		    Element exceptionElement =
		            new Element("OfertaReservaDateException", XML_NS);
		
		    if(ex.getOfertaId() != null) {
		        Element ofertaIdElement = new Element("ofertaId", XML_NS);
		        ofertaIdElement.setText(ex.getOfertaId().toString());
		        exceptionElement.addContent(ofertaIdElement);
		    }
		
		    return new Document(exceptionElement);
	}
	
	public static Document toOfertaReclamaDateException(
			OfertaReclamaDateException ex) 
        throws IOException {

		    Element exceptionElement =
		            new Element("OfertaReclamaDateException", XML_NS);
		
		    if(ex.getOfertaId() != null) {
		        Element ofertaIdElement = new Element("ofertaId", XML_NS);
		        ofertaIdElement.setText(ex.getOfertaId().toString());
		        exceptionElement.addContent(ofertaIdElement);
		    }
		
		    return new Document(exceptionElement);
	}
	
}
