package es.udc.ws.app.soapservice;


public class SoapReservaEstadoExceptionInfo {

    private Long reservaId;
    private String estado;

    public SoapReservaEstadoExceptionInfo() {
    }

    public SoapReservaEstadoExceptionInfo(Long reservaId, 
                                           String estado) {
        this.reservaId = reservaId;
        this.estado = estado;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }    
    
}
