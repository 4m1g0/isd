package es.udc.ws.app.model.oferta;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlOfertaDao {

    public Oferta create(Connection connection, Oferta oferta);

    public Oferta find(Connection connection, Long ofertaId)
            throws InstanceNotFoundException;

    public List<Oferta> findByKeywords(Connection connection,
            String keywords);

    public void update(Connection connection, Oferta oferta)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long ofertaId)
            throws InstanceNotFoundException;
}
