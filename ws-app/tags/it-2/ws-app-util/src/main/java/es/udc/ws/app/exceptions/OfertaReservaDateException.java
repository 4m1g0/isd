package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfertaReservaDateException extends Exception {

    private Long ofertaId;

    public OfertaReservaDateException(Long ofertaId) {
        super("Oferta with id=\"" + ofertaId + 
              "\" cant be reserved actually");
        this.ofertaId = ofertaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }
}