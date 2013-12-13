package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfertaEmailException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfertaEmailException extends Exception {

    private SoapOfertaEmailExceptionInfo faultInfo;

    protected SoapOfertaEmailException(
            SoapOfertaEmailExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfertaEmailExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}