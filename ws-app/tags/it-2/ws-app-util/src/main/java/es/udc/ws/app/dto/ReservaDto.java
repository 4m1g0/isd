package es.udc.ws.app.dto;

import java.util.Calendar;

public class ReservaDto {

    private Long reservaId;
    private Long ofertaId;
    private Short estado;
    //public final static short ESTADO_PENDIENTE = 0;
    //public final static short ESTADO_CERRADA = 1;
    private Calendar fechaReserva;
    
    public ReservaDto() {
    }    
    
    public ReservaDto(Long reservaId, Long ofertaId, Short estado, Calendar fechaReserva) {
    	this.reservaId = reservaId;
    	this.ofertaId = ofertaId;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
        if (fechaReserva != null) {
            this.fechaReserva.set(Calendar.MILLISECOND, 0);
        }
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    public Calendar getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Calendar fechaReserva) {
        this.fechaReserva = fechaReserva;
        if (fechaReserva != null) {
            this.fechaReserva.set(Calendar.MILLISECOND, 0);
        }
    }

    public Short getEstado() {
		return estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

}
