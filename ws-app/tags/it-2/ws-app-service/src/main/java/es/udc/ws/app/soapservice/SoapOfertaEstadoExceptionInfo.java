package es.udc.ws.app.soapservice;


public class SoapOfertaEstadoExceptionInfo {

    private Long ofertaId;
    private short estado;

    public SoapOfertaEstadoExceptionInfo() {
    }

    public SoapOfertaEstadoExceptionInfo(Long ofertaId, 
                                           short estado) {
        this.ofertaId = ofertaId;
        this.estado = estado;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
