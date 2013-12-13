package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfertaMaxPersonasException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaMaxPersonasException extends Exception {

    private SoapOfertaMaxPersonasExceptionInfo faultInfo;

    protected SoapOfertaMaxPersonasException(
            SoapOfertaMaxPersonasExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfertaMaxPersonasExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}