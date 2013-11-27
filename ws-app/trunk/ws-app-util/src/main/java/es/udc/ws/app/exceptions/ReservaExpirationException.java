package es.udc.ws.app.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ReservaExpirationException extends Exception {

    private Long reservaId;
    private Calendar expirationDate;

    public ReservaExpirationException(Long reservaId, Calendar expirationDate) {
        super("Reserva with id=\"" + reservaId + 
              "\" has expired (expirationDate = \"" + 
              expirationDate + "\")");
        this.reservaId = reservaId;
        this.expirationDate = expirationDate;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }
}