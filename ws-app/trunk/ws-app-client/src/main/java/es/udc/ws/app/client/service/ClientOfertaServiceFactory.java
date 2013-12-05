package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientOfertaServiceFactory {

    private final static String CLASS_NAME_PARAMETER = 
            "ClientOfertaServiceFactory.className";
    private static Class<ClientOfertaService> serviceClass = null;

    private ClientOfertaServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientOfertaService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientOfertaService>) 
                        Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientOfertaService getService() {

        try {
            return (ClientOfertaService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
