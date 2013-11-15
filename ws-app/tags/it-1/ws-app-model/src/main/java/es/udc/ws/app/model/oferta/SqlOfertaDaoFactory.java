package es.udc.ws.app.model.oferta;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlOfertaDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlOfertaDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlOfertaDao</code>.</li> </ul>
 */
public class SqlOfertaDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlOfertaDaoFactory.className";
    private static SqlOfertaDao dao = null;

    private SqlOfertaDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlOfertaDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlOfertaDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlOfertaDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}