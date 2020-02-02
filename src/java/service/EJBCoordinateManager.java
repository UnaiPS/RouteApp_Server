package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Type;

/**
 * The implementation for the CoordinateManagerLocal.
 *
 * @author Jon Calvo Gaminde
 */
@Stateless
public class EJBCoordinateManager implements CoordinateManagerLocal {

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    private Logger LOGGER = Logger.getLogger("EJBCoordinateManager");

    /**
     * A method that finds the directions by coordinate type in the Database.
     *
     * @param type The type of the coordinates.
     * @return The directions found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<Direction> findDirectionsByType(String type) throws FindException {
        List<Direction> directions = null;
        try {
            directions = em.createNamedQuery("findDirectionsByType").setParameter("type", Type.valueOf(type)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return directions;
    }

    /**
     * A method that finds the directions by a route in the Database.
     *
     * @param routeId The id of the route.
     * @return The directions found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<Direction> findDirectionsByRoute(String routeId) throws FindException {
        List<Direction> directions = null;
        try {
            directions = em.createNamedQuery("findDirectionsByRoute").setParameter("routeId", Long.parseLong(routeId)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return directions;
    }

    /**
     * A method that inserts and sets a visited GPS coodinate to a
     * coordinate_route in the Database.
     *
     * @param gps The GPS coordinate.
     * @param routeId The routeId of the coordinate_route.
     * @param coordinateId The coordinateId of the coordinate_route.
     * @return The new id of the GPS coordinate.
     * @throws UpdateException An exception thrown if there was an error
     * updating the entity.
     */
    @Override
    public Long updateCoordinateRoute(Coordinate gps, Long routeId, Long coordinateId) throws UpdateException {
        try {
            List<Coordinate_Route> coordinate_routes = null;
            gps.setId(createCoordinate(gps));
            coordinate_routes = em.createNamedQuery("findCoordinateRoutesByCoordinateId").setParameter("id", coordinateId).getResultList();
            Coordinate_Route visited = coordinate_routes.stream().filter(cr -> cr.getId().getRouteId() == routeId.longValue()).findAny().get();
            visited.setVisited(gps.getId());
            em.merge(visited);
            em.flush();
            return gps.getId();
        } catch (Exception e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * A method that inserts a coordinate in the Database.
     *
     * @param coordinate The coordinate to insert.
     * @return The new id of the coordinate.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    @Override
    public Long createCoordinate(Coordinate coordinate) throws CreateException {
        try {
            Long id = getIdByData(coordinate);
            if (id == null) {
                em.persist(coordinate);
                id = coordinate.getId();
            }
            return id;
        } catch (Exception e) {
            throw new CreateException(e.getMessage() + "Id es: " + coordinate.getId());
        }

    }

    /**
     * A method that updates a coordinate in the Database.
     *
     * @param coordinate The coordinate to update.
     * @throws UpdateException An exception thrown if there was an error
     * updating the entity.
     */
    @Override
    public void updateCoordinate(Coordinate coordinate) throws UpdateException {
        try {
            em.merge(coordinate);
            em.flush();
        } catch (Exception e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * A method that removes a coordinate in the Database.
     *
     * @param coordinate The coordinate to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    @Override
    public void removeCoordinate(Coordinate coordinate) throws DeleteException {
        try {
            if (!coordinate.getType().equals(Type.WAYPOINT)) {
                try {
                    Direction direction = (Direction) em.createNamedQuery("findDirectionByCoordinate").setParameter("coordinate", coordinate).getSingleResult();
                    em.remove(em.merge(direction));

                } catch (NoResultException e) {
                    LOGGER.info("No direction exists for this coordinate: " + coordinate.toString());
                }
            }
            em.remove(em.merge(coordinate));
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }

    /**
     * A method that finds a coordinate by id in the Database.
     *
     * @param id The id of the coordinate.
     * @return The found coordinate.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public Coordinate findCoordinate(Long id) throws FindException {
        try {
            return em.find(Coordinate.class, id);
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }

    }

    /**
     * A method that finds the coordinate by type in the Database.
     *
     * @param type The type of the coordinates.
     * @return The coordinate found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<Coordinate> findByType(String type) throws FindException {
        List<Coordinate> coords = null;
        try {
            coords = em.createNamedQuery("findCoordinatesByType").setParameter("type", Type.valueOf(type)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return coords;
    }

    /**
     * A method that finds the coordinate id by the rest of the data in the
     * Database.
     *
     * @param coordinate The data of the coordinate.
     * @return The coordinate id.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public Long getIdByData(Coordinate coordinate) throws FindException {
        try {
            return (Long) em.createNamedQuery("getCoordinateIdByData")
                    .setParameter("latitude", coordinate.getLatitude())
                    .setParameter("longitude", coordinate.getLongitude())
                    .setParameter("type", coordinate.getType()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
    }

    /**
     * A method that creates a direction in the Database.
     *
     * @param direction The direction to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    @Override
    public void createDirection(Direction direction) throws CreateException {
        try {
            direction.setCoordinate(findCoordinate(getIdByData(direction.getCoordinate())));
            em.createNamedQuery("findDirectionByCoordinate").setParameter("coordinate", direction.getCoordinate()).getSingleResult();
            LOGGER.info("Direction already exists.");

        } catch (NoResultException e) {
            em.persist(direction);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * A method that creates a coordinate_route in the Database.
     *
     * @param segment The coordinate_route to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    @Override
    public void createCoordinateRoute(Coordinate_Route segment) throws CreateException {
        try {
            segment.getCoordinate().setId(createCoordinate(segment.getCoordinate()));
            segment.setCoordinate(findCoordinate(segment.getCoordinate().getId()));
            em.persist(segment);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * A method that removes a coordinate_route in the Database.
     *
     * @param segment The coordinate_route to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    @Override
    public void removeCoordinateRoute(Coordinate_Route segment) throws DeleteException {
        try {
            Coordinate coordinate = segment.getCoordinate();
            em.remove(em.merge(segment));

            try {
                em.createNamedQuery("findCoordinateRoutesByCoordinateId").setParameter("id", coordinate.getId()).getSingleResult();
            } catch (NoResultException e) {
                removeCoordinate(coordinate);
            }
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }

}
