package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfertaEmailException extends Exception {

    private Long ofertaId;
    private String emailUsuario;

    public OfertaEmailException(Long ofertaId, String emailUsuario) {
        super("Oferta with id=\"" + ofertaId + 
              "\" has a (emailUsuario = \"" + 
              emailUsuario + "\") identical in the database");
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