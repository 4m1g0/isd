package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfertaReclamaException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaReclamaDateException extends Exception {

    private SoapOfertaReclamaDateExceptionInfo faultInfo;

    protected SoapOfertaReclamaDateException(
            SoapOfertaReclamaDateExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfertaReclamaDateExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}