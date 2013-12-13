package es.udc.ws.app.soapservice;


public class SoapOfertaMaxPersonasExceptionInfo {

    private Long ofertaId;
    private short maxPersonas;

    public SoapOfertaMaxPersonasExceptionInfo() {
    }

    public SoapOfertaMaxPersonasExceptionInfo(Long ofertaId, 
                                           short maxPersonas) {
        this.ofertaId = ofertaId;
        this.maxPersonas = maxPersonas;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public short getMaxPersonas() {
        return maxPersonas;
    }

    public void setMaxPersonas(short maxPersonas) {
        this.maxPersonas = maxPersonas;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
