package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfertaEstadoException extends Exception {

    private Long ofertaId;
    private short estado;

    public OfertaEstadoException(Long ofertaId, short estado) {
        super("Oferta with id=\"" + ofertaId + 
              "\" has a state problem (estado = \"" + 
              estado + "\")");
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