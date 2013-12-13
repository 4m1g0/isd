package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfertaEstadoException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaEstadoException extends Exception {

    private SoapOfertaEstadoExceptionInfo faultInfo;

    protected SoapOfertaEstadoException(
            SoapOfertaEstadoExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfertaEstadoExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}