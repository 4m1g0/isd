package es.udc.ws.app.model.ofertaservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class OfertaServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "OfertaServiceFactory.className";
    private static OfertaService service = null;

    private OfertaServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static OfertaService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (OfertaService) serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static OfertaService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
