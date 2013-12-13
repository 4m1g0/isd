package es.udc.ws.app.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class OfertaDto {

    private Long ofertaId;
    private String titulo;
    private String descripcion;
    private Calendar iniReserva;
    private Calendar limReserva; 
    private Calendar limOferta;
    private float precioReal;
    private float precioRebajado;
    private Short maxPersonas;

    public OfertaDto() {
    }    
    
    public OfertaDto(Long ofertaId, String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas) {

        this.ofertaId = ofertaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.iniReserva = iniReserva;
        if (iniReserva != null) {
            this.iniReserva.set(Calendar.MILLISECOND, 0);
        }
        this.limReserva = limReserva;
        if (limReserva != null) {
            this.limReserva.set(Calendar.MILLISECOND, 0);
        }
        this.limOferta = limOferta;
        if (limOferta != null) {
            this.limOferta.set(Calendar.MILLISECOND, 0);
        }
        this.precioReal = precioReal;
        this.precioRebajado = precioRebajado;
        this.maxPersonas = maxPersonas;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long id) {
        this.ofertaId = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(float precioReal) {
        this.precioReal = precioReal;
    }

	public float getPrecioRebajado() {
		return precioRebajado;
	}

	public void setPrecioRebajado(float precioRebajado) {
		this.precioRebajado = precioRebajado;
	}
	
    public Calendar getIniReserva() {
        return iniReserva;
    }
    
    public void setIniReserva(Calendar iniReserva) {
        this.iniReserva = iniReserva;
        if (iniReserva != null) {
            this.iniReserva.set(Calendar.MILLISECOND, 0);
        }
    }
    
    public Calendar getLimReserva() {
		return limReserva;
	}

	public void setLimReserva(Calendar limReserva) {
		this.limReserva = limReserva;
        if (limReserva != null) {
            this.limReserva.set(Calendar.MILLISECOND, 0);
        }
    }

	public Calendar getLimOferta() {
		return limOferta;
	}

	public void setLimOferta(Calendar limOferta) {
		this.limOferta = limOferta;
        if (limOferta != null) {
            this.limOferta.set(Calendar.MILLISECOND, 0);
        }
    }

    public Short getMaxPersonas() {
		return maxPersonas;
	}

	public void setMaxPersonas(Short maxPersonas) {
        if (maxPersonas == null)
            this.maxPersonas = Short.MAX_VALUE;
        else
        	this.maxPersonas = maxPersonas;
	}


    @Override
    public String toString() {
    	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");  

    	return "OfertaDto [ofertaId=" + ofertaId + ", titulo=" + titulo
                + ", descripcion=" + descripcion + ", iniReserva=" + formatter.format(iniReserva.getTime())
                + ", limReserva=" + formatter.format(limReserva.getTime())
                + ", limOferta=" + formatter.format(limOferta.getTime())
                + ", precioReal=" + precioReal + ", precioRebajado=" + precioRebajado 
                + ", maxPersonas=" + maxPersonas + "]";
    }



}
