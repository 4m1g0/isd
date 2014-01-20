package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.app.dto.ReservaDto.Estado;
import es.udc.ws.app.model.reserva.Reserva;

public class ReservaToReservaDtoConversor {
    
    public static List<ReservaDto> toReservaDtos(List<Reserva> reservas) {
        List<ReservaDto> reservaDtos = new ArrayList<>(reservas.size());
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            reservaDtos.add(toReservaDto(reserva));
        }
        return reservaDtos;
    }
    
    public static ReservaDto toReservaDto(Reserva reserva) {
    	if (reserva.getEstado() != null)
    		return new ReservaDto(reserva.getReservaId(), reserva.getOfertaId(), Estado.valueOf(reserva //parseamos por enum diferentes
                .getEstado().name()), reserva.getFechaReserva());
    	return new ReservaDto(reserva.getReservaId(), reserva.getOfertaId(), null, reserva.getFechaReserva());
    }
    
}
