package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfertaReservaDateException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaReservaDateException extends Exception {

    private SoapOfertaReservaDateExceptionInfo faultInfo;

    protected SoapOfertaReservaDateException(
            SoapOfertaReservaDateExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfertaReservaDateExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}