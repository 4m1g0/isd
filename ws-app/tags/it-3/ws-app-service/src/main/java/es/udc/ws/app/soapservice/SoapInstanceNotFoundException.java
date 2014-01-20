package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapInstanceNotFoundException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapInstanceNotFoundException extends Exception {

    private SoapInstanceNotFoundExceptionInfo faultInfo;  
    
    protected SoapInstanceNotFoundException(
            SoapInstanceNotFoundExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapInstanceNotFoundExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
