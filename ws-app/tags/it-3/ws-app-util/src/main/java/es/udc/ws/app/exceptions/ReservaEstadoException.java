package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class ReservaEstadoException extends Exception {

    private Long reservaId;
    private String estado;

    public ReservaEstadoException(Long reservaId, String estado) {
        super("Reserva with id=\"" + reservaId + 
              "\" has a state problem (estado = \"" + 
              estado + "\")");
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