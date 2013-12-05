package es.udc.ws.app.soapservice;

import java.util.Calendar;

public class SoapReservaExpirationExceptionInfo {

    private Long reservaId;
    private Calendar expirationDate;

    public SoapReservaExpirationExceptionInfo() {
    }

    public SoapReservaExpirationExceptionInfo(Long reservaId, 
                                           Calendar expirationDate) {
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
