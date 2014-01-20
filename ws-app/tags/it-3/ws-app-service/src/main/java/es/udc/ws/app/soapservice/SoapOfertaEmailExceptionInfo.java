package es.udc.ws.app.soapservice;


public class SoapOfertaEmailExceptionInfo {

    private Long ofertaId;
    private String emailUsuario;

    public SoapOfertaEmailExceptionInfo() {
    }

    public SoapOfertaEmailExceptionInfo(Long ofertaId, 
                                           String emailUsuario) {
        this.ofertaId = ofertaId;
        this.emailUsuario = emailUsuario;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }    
    
}
