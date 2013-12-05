package es.udc.ws.app.client.ui;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.client.service.ClientOfertaService;
import es.udc.ws.app.client.service.ClientOfertaServiceFactory;
import es.udc.ws.app.dto.OfertaDto;
import es.udc.ws.app.dto.ReservaDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class OfertaServiceClient {

	private static Calendar DateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		}
	
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientOfertaService clientOfertaService =
                ClientOfertaServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 10, new int[] {3, 4, 5, 6, 7, 8});

            // [add] OfertaServiceClient -a <titulo> <descripcion> <iniReserva> <limReserva> <limOferta> <precioReal> <precioRebajado> <maxPersonas>

            try {
                Long ofertaId = clientOfertaService.addOferta(new OfertaDto(null,
                        args[1], args[2], DateToCalendar(Date.valueOf(args[3])), DateToCalendar(Date.valueOf(args[4])), 
                        DateToCalendar(Date.valueOf(args[5])), Float.valueOf(args[6]), Float.valueOf(args[7]), Short.valueOf(args[8])));

                System.out.println("Oferta " + ofertaId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-r".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [remove] OfertaServiceClient -r <ofertaId>

            try {
                clientOfertaService.removeOferta(Long.parseLong(args[1]));

                System.out.println("Oferta with id " + args[1] +
                        " removed sucessfully");

            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-u".equalsIgnoreCase(args[0])) {
           validateArgs(args, 11, new int[] {1, 4, 5, 6, 7, 8, 9});

           // [update] OfertaServiceClient -u <ofertaId> <titulo> <descripcion> <iniReserva> <limReserva> <limOferta> <precioReal> <precioRebajado> <maxPersonas>

           try {
                clientOfertaService.updateOferta(new OfertaDto(
                        Long.valueOf(args[1]),
                        args[2], args[3], DateToCalendar(Date.valueOf(args[4])), DateToCalendar(Date.valueOf(args[5])), 
                        DateToCalendar(Date.valueOf(args[6])), Float.valueOf(args[7]), Float.valueOf(args[8]), Short.valueOf(args[9])));

                System.out.println("Oferta " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fo".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2, 3});

            // [findOferta] OfertaServiceClient -fo <ofertaId>

            try {
                OfertaDto ofertaDto = clientOfertaService.findOferta(Long.valueOf(args[1]));
                    System.out.println("Id: " + ofertaDto.getOfertaId() +
                            " Titulo: " + ofertaDto.getTitulo() +
                            " Descripcion: " + ofertaDto.getDescripcion() +
                            " IniReserva: " + ofertaDto.getIniReserva() +
                            " LimReserva: " + ofertaDto.getLimReserva() +
                            " LimOferta: " + ofertaDto.getLimOferta() +
                            " PrecioReal: " + ofertaDto.getPrecioReal() +
                    		" PrecioRebajado: " + ofertaDto.getPrecioRebajado() +
                            " MaxPersonas: " + ofertaDto.getMaxPersonas());


                
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fos".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2, 3});

            // [findOfertas] OfertaServiceClient -f [keywords] [estado] [fecha]

            try {
                List<OfertaDto> ofertas = clientOfertaService.findOfertas(args[1], Short.valueOf(args[2]), DateToCalendar(Date.valueOf(args[3]))); //FIXME
                System.out.println("Found " + ofertas.size() +
                        " oferta(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < ofertas.size(); i++) {
                    OfertaDto ofertaDto = ofertas.get(i);
                    System.out.println("Id: " + ofertaDto.getOfertaId() +
                            " Titulo: " + ofertaDto.getTitulo() +
                            " Descripcion: " + ofertaDto.getDescripcion() +
                            " IniReserva: " + ofertaDto.getIniReserva() +
                            " LimReserva: " + ofertaDto.getLimReserva() +
                            " LimOferta: " + ofertaDto.getLimOferta() +
                            " PrecioReal: " + ofertaDto.getPrecioReal() +
                    		" PrecioRebajado: " + ofertaDto.getPrecioRebajado() +
                            " MaxPersonas: " + ofertaDto.getMaxPersonas());


                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-re".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {1});

            // [reservar] OfertaServiceClient -re <ofertaId> <email> <numeroTarjeta>

            Long reservaId;
            try {
                reservaId = clientOfertaService.reservarOferta(Long.parseLong(args[1]),
                        args[2], args[3]);

                System.out.println("Oferta " + args[1] +
                        " reservada sucessfully with reserva number " +
                        reservaId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fr".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [find] OfertaServiceClient -fr <reservaId>

            try {
                ReservaDto reservaDto = clientOfertaService.findReserva(Long.parseLong(args[1]));
                    System.out.println("reservaId: " + reservaDto.getReservaId() +
                            " ofertaId: " + reservaDto.getOfertaId() +
                            " Estado: " + reservaDto.getEstado() +
                            " FechaReserva: " + reservaDto.getFechaReserva());
                    
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-frs".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1, 2});

            // [find] OfertaServiceClient -frs <ofertaId> <estado>

            try {
                List<ReservaDto> reservas = clientOfertaService.findReservas(Long.parseLong(args[1]), Short.valueOf(args[2]));
                System.out.println("Found " + reservas.size() +
                        " reservas with id '" + args[1] + "'");
                for (int i = 0; i < reservas.size(); i++) {
                    ReservaDto reservaDto = reservas.get(i);
                    System.out.println("reservaId: " + reservaDto.getReservaId() +
                            " ofertaId: " + reservaDto.getOfertaId() +
                            " Estado: " + reservaDto.getEstado() +
                            " FechaReserva: " + reservaDto.getFechaReserva());
                            }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-ro".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {1});

            // [reclamarOferta] OfertaServiceClient -ro <reservaId>

            try {
                if (!clientOfertaService.reclamarOferta(Long.parseLong(args[1])))
                	System.out.println("Oferta " + args[1] + " reclamacion fallada..");
                else
                	System.out.println("Oferta " + args[1] + " reclamada sucessfully!");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "  [add]            OfertaServiceClient -a <titulo> <descripcion> <iniReserva> <limReserva> <limOferta> <precioReal> <precioRebajado> <maxPersonas>\n" +
                "  [remove]         OfertaServiceClient -r <ofertaId>\n" +
                "  [update]         OfertaServiceClient -u <ofertaId> <titulo> <descripcion> <iniReserva> <limReserva> <limOferta> <precioReal> <precioRebajado> <maxPersonas>\n" +
                "  [findOferta]     OfertaServiceClient -fo <ofertaId>\n" +  
                "  [findOfertas]    OfertaServiceClient -fos [keywords] [estado] [fecha]\n" +
                "  [reservar]       OfertaServiceClient -re <ofertaId> <email> <numeroTarjeta>\n" +
                "  [findReserva]    OfertaServiceClient -fr <reservaId>\n" +
                "  [findReservas]   OfertaServiceClient -frs <ofertaId> <estado>\n" +
                "  [reclamarOferta] OfertaServiceClient -ro <reservaId> ");
                }

}
