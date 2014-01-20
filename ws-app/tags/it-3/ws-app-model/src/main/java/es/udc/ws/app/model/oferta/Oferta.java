package es.udc.ws.app.model.oferta;

import java.util.Calendar;

public class Oferta {

    private Long ofertaId;
    private String titulo;
    private String descripcion;
    private Calendar iniReserva;
    private Calendar limReserva; 
    private Calendar limOferta;
    private float precioReal;
    private float precioRebajado;
    private Long maxPersonas;
	
	public enum Estado{CREADA, COMPROMETIDA, LIBERADA};
	private Estado estado;
	
	//Atributos para mejorar el rendimiento
	private Long numReservas;
	private Long numUsedReservas;	

    
    public Oferta(String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, Long maxPersonas, Estado estado, Long numReservas, Long numUsedReservas) 
    {
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
        if (maxPersonas == null)
            this.maxPersonas = Long.MAX_VALUE;
        else
        	this.maxPersonas = maxPersonas;
        this.estado = estado;
        this.numReservas = numReservas;
        this.numUsedReservas = numUsedReservas;
    }
    
	public Oferta(Long ofertaId, String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, Long maxPersonas, Estado estado, Long numReservas, Long numUsedReservas) 
    {
        this(titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado, numReservas, numUsedReservas);
        this.ofertaId = ofertaId;
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

    public Long getMaxPersonas() {
		return maxPersonas;
	}

	public void setMaxPersonas(Long maxPersonas) {
        if (maxPersonas == null)
            this.maxPersonas = Long.MAX_VALUE;
        else
        	this.maxPersonas = maxPersonas;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public Long getNumReservas() {
		return numReservas;
	}

	public void setNumReservas(Long numReservas) {
		this.numReservas = numReservas;
	}

	public Long getNumUsedReservas() {
		return numUsedReservas;
	}

	public void setNumUsedReservas(Long numUsedReservas) {
		this.numUsedReservas = numUsedReservas;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((iniReserva == null) ? 0 : iniReserva.hashCode());
        result = prime * result
                + ((limReserva == null) ? 0 : limReserva.hashCode());
        result = prime * result
                + ((limOferta == null) ? 0 : limOferta.hashCode()); 
        result = prime * result
                + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((ofertaId == null) ? 0 : ofertaId.hashCode());
        result = prime * result + Float.floatToIntBits(precioReal);
        result = prime * result + ((maxPersonas == null) ? 0 : maxPersonas.hashCode());
        result = prime * result + estado.hashCode();
        result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
        result = prime * result + ((numReservas == null) ? 0 : numReservas.hashCode());
        result = prime * result + ((numUsedReservas == null) ? 0 : numUsedReservas.hashCode());

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
        Oferta other = (Oferta) obj;
        if (iniReserva == null) {
            if (other.iniReserva != null) {
                return false;
            }
        } else if (!iniReserva.equals(other.iniReserva)) {
            return false;
        }
        if (limReserva == null) {
            if (other.limReserva != null) {
                return false;
            }
        } else if (!limReserva.equals(other.limReserva)) {
            return false;
        }
        if (limOferta == null) {
            if (other.limOferta != null) {
                return false;
            }
        } else if (!limOferta.equals(other.limOferta)) {
            return false;
        }
        if (descripcion == null) {
            if (other.descripcion != null) {
                return false;
            }
        } else if (!descripcion.equals(other.descripcion)) {
            return false;
        }
        if (ofertaId == null) {
            if (other.ofertaId != null) {
                return false;
            }
        } else if (!ofertaId.equals(other.ofertaId)) {
            return false;
        }
        if (Float.floatToIntBits(precioReal) != Float.floatToIntBits(other.precioReal)) {
            return false;
        }
        if (Float.floatToIntBits(precioRebajado) != Float.floatToIntBits(other.precioRebajado)) {
            return false;
        }
        if (maxPersonas == null) {
            if (other.maxPersonas != null) {
                return false;
            }
        } else if (!maxPersonas.equals(other.maxPersonas)) {
            return false;
        }
        if (estado != other.estado) {
                return false;        
        }
        if (titulo == null) {
            if (other.titulo != null) {
                return false;
            }
        } else if (!titulo.equals(other.titulo)) {
            return false;
        }
        if (numReservas == null) {
            if (other.numReservas != null) {
                return false;
            }
        } else if (!numReservas.equals(other.numReservas)) {
            return false;
        }
        if (numUsedReservas == null) {
            if (other.numUsedReservas != null) {
                return false;
            }
        } else if (!numUsedReservas.equals(other.numUsedReservas)) {
            return false;
        }
        return true;
    }
}