package es.udc.ws.app.model.reserva;

import java.util.Calendar;

public class Reserva {

    private Long reservaId;
    private Long ofertaId;
    private String emailUsuario;
    private String numeroTarjeta;
    private Short estado;
    public final static short ESTADO_PENDIENTE = 0;
    public final static short ESTADO_CERRADA = 1;
    private Calendar fechaReserva;

    public Reserva(Long ofertaId, String emailUsuario, String numeroTarjeta, Short estado, Calendar fechaReserva) 
    {
        this.ofertaId = ofertaId;
        this.emailUsuario = emailUsuario;
        this.numeroTarjeta = numeroTarjeta;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
        if (fechaReserva != null) {
            this.fechaReserva.set(Calendar.MILLISECOND, 0);
        }
    }

    public Reserva(Long reservaId, Long ofertaId, String emailUsuario,
    		String numeroTarjeta, Short estado, Calendar fechaReserva) {
        this(ofertaId, emailUsuario, numeroTarjeta, estado, fechaReserva);
        this.reservaId = reservaId;
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

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
    
    public Short getEstado() {
		return estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((numeroTarjeta == null) ? 0 : numeroTarjeta.hashCode());
        result = prime * result
                + ((fechaReserva == null) ? 0 : fechaReserva.hashCode());
        result = prime * result + ((ofertaId == null) ? 0 : ofertaId.hashCode());
        result = prime * result
                + ((emailUsuario == null) ? 0 : emailUsuario.hashCode());
        result = prime * result + ((reservaId == null) ? 0 : reservaId.hashCode());
        result = prime * result + estado;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Reserva other = (Reserva) obj;
        if (numeroTarjeta == null) {
            if (other.numeroTarjeta != null) {
                return false;
            }
        } else if (!numeroTarjeta.equals(other.numeroTarjeta)) {
            return false;
        }

        if (fechaReserva == null) {
            if (other.fechaReserva != null) {
                return false;
            }
        } else if (!fechaReserva.equals(other.fechaReserva)) {
            return false;
        }
        if (ofertaId == null) {
            if (other.ofertaId != null) {
                return false;
            }
        } else if (!ofertaId.equals(other.ofertaId)) {
            return false;
        }
        if (emailUsuario == null) {
            if (other.emailUsuario != null) {
                return false;
            }
        } else if (!emailUsuario.equals(other.emailUsuario)) {
            return false;
        }
        if (estado == null) {
            if (other.estado != null) {
                return false;
            }
        } else if (!estado.equals(other.estado)) {
            return false;
        }        

        if (reservaId == null) {
            if (other.reservaId != null) {
                return false;
            }
        } else if (!reservaId.equals(other.reservaId)) {
            return false;
        }

        return true;
    }
}
