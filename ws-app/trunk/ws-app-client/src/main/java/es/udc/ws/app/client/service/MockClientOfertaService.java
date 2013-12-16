package es.udc.ws.app.client.service;

import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MockClientOfertaService implements ClientOfertaService {

    @Override
    public Long addOferta(OfertaDto oferta) {
        // TODO Auto-generated method stub
        return (long) 0;
    }

    @Override
    public void updateOferta(OfertaDto oferta) {

    }

    @Override
    public void removeOferta(Long ofertaId) {
        // TODO Auto-generated method stub

    }
	
	@Override
	public OfertaDto findOferta(Long ofertaId) throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
    public List<OfertaDto> findOfertas(String keywords) {

        List<OfertaDto> ofertas = new ArrayList<>();

        ofertas.add(new OfertaDto(1L, "oferta1",
                 "descripcion", Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
                 10F, 8F, (short) 9));
        ofertas.add(new OfertaDto(2L, "oferta2",
                 "descripcion2", Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
                 10F, 8F, (short) 9));

        return ofertas;

    }
    
    @Override
    public Long reservarOferta(Long ofertaId, String userId, String creditCardNumber) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public List<ReservaDto> findReservas(Long ofertaId, Short estado)
			throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservaDto findReserva(Long reservaId)
			throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reclamarOferta(Long reservaId)
			throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
