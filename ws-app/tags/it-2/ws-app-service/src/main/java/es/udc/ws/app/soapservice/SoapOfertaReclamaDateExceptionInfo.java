package es.udc.ws.app.soapservice;


public class SoapOfertaReclamaDateExceptionInfo {

    private Long ofertaId;

    public SoapOfertaReclamaDateExceptionInfo() {
    }

    public SoapOfertaReclamaDateExceptionInfo(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
