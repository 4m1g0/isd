package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapReservaEstadoException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapReservaEstadoException extends Exception {

    private SoapReservaEstadoExceptionInfo faultInfo;

    protected SoapReservaEstadoException(
            SoapReservaEstadoExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapReservaEstadoExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}