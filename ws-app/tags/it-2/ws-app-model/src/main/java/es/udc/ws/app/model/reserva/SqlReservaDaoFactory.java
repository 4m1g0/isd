package es.udc.ws.app.model.reserva;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlReservaDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlReservaDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlReservaDao</code>.</li> </ul>
 */
public class SqlReservaDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlReservaDaoFactory.className";
    private static SqlReservaDao dao = null;

    private SqlReservaDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlReservaDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlReservaDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlReservaDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
