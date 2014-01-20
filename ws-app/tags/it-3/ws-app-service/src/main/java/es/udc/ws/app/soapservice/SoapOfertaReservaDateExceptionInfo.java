package es.udc.ws.app.soapservice;


public class SoapOfertaReservaDateExceptionInfo {

    private Long ofertaId;

    public SoapOfertaReservaDateExceptionInfo() {
    }

    public SoapOfertaReservaDateExceptionInfo(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
