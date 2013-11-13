package es.udc.ws.app.model.oferta;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlOfertaDao {

    public Oferta create(Connection connection, Oferta movie);

    public Oferta find(Connection connection, Long movieId)
            throws InstanceNotFoundException;

    public List<Oferta> findByKeywords(Connection connection,
            String keywords);

    public void update(Connection connection, Oferta movie)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long movieId)
            throws InstanceNotFoundException;
}
