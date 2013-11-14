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
    private Short maxPersonas;
    private Short estado; // FIXME
    private final Short s0; // Creada - La oferta ha sido creada pero aún no ha sido reservada por nadie.
    private final Short s1; // Comprometida - La oferta ha sido reservada por algún usuario, pero aún no ha sido reclamada por todos ellos.
    private final Short s2 ; // Liberada - La oferta ha sido reclamada por todos los usuarios que la reservaron hasta el momento.

    public Oferta(String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas, short estado) 
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
        this.maxPersonas = maxPersonas;
        this.estado = estado;
        this.s0 = 0;
        this.s1 = 1;
        this.s2 = 2;
    }
    
	public Oferta(Long ofertaId, String titulo, String descripcion, Calendar iniReserva, Calendar limReserva, Calendar limOferta, float precioReal, float precioRebajado, short maxPersonas, short estado) 
    {
        this(titulo, descripcion, iniReserva, limReserva, limOferta, precioReal, precioRebajado, maxPersonas, estado);
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

    public Short getMaxPersonas() {
		return maxPersonas;
	}

	public void setMaxPersonas(Short maxPersonas) {
        if (maxPersonas == null)
            this.maxPersonas = Short.MAX_VALUE;
        else
        	this.maxPersonas = maxPersonas;
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
        result = prime * result + maxPersonas;
        result = prime * result + estado;
        result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
        return true;
    }
}