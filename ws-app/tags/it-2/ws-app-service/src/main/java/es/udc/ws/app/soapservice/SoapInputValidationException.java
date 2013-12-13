package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapInputValidationException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapInputValidationException extends Exception {  
    
    public SoapInputValidationException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}
