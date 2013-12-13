package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfertaReclamaDateException extends Exception {

    private Long ofertaId;
    
    public OfertaReclamaDateException(Long ofertaId) {
        super("Oferta with id=\"" + ofertaId + 
              "\" has expired, cant be reclaimed");
        this.ofertaId = ofertaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }
}