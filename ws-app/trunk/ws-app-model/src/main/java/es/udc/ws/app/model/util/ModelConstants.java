package es.udc.ws.app.model.util;

public final class ModelConstants {

    /*public static final String MOVIE_DATA_SOURCE = "ws-javaexamples-ds";
    public static final int SALE_EXPIRATION_DAYS = 2;
    public static final String BASE_URL = "http://ws-movies.udc.es/sale/stream/";
    public static final short MAX_RUNTIME = 1000;
    public static final float MAX_PRICE = 1000;*/
	
	public static final String OFERTA_DATA_SOURCE = "ws-javaexamples-ds";
    public static final String BASE_URL = "http://ws-ofertas.udc.es/reserva/stream/";
    public static final int PRECIO_REAL_MAXIMO = 3000;
    public static final int PRECIO_REBAJADO_MAXIMO = 2500;
    public static final int MAX_PERSONAS = 10;
    public static final int NUM_ESTADOS = 2;
    public static final int RESERVA_EXPIRATION_DAYS = 10;

    private ModelConstants() {
    }
}
