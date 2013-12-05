package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapReservaExpirationException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapReservaExpirationException extends Exception {

    private SoapReservaExpirationExceptionInfo faultInfo;

    protected SoapReservaExpirationException(
            SoapReservaExpirationExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapReservaExpirationExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}