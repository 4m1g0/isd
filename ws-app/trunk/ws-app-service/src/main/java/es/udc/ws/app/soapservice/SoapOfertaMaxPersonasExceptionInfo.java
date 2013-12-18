package es.udc.ws.app.soapservice;


public class SoapOfertaMaxPersonasExceptionInfo {

    private Long ofertaId;
    private Long maxPersonas;

    public SoapOfertaMaxPersonasExceptionInfo() {
    }

    public SoapOfertaMaxPersonasExceptionInfo(Long ofertaId, 
                                           Long maxPersonas) {
        this.ofertaId = ofertaId;
        this.maxPersonas = maxPersonas;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public Long getMaxPersonas() {
        return maxPersonas;
    }

    public void setMaxPersonas(Long maxPersonas) {
        this.maxPersonas = maxPersonas;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
