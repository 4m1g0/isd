package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfertaMaxPersonasException extends Exception {

    private Long ofertaId;
    private short maxPersonas;

    public OfertaMaxPersonasException(Long ofertaId, short maxPersonas) {
        super("Oferta with id=\"" + ofertaId + 
              "\" has the (maxPersonas = \"" + 
              maxPersonas + "\") limit");
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