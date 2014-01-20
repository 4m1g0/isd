package es.udc.ws.app.soapservice;


public class SoapOfertaEstadoExceptionInfo {

    private Long ofertaId;
    private String estado;

    public SoapOfertaEstadoExceptionInfo() {
    }

    public SoapOfertaEstadoExceptionInfo(Long ofertaId, 
                                           String estado) {
        this.ofertaId = ofertaId;
        this.estado = estado;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
