package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.EdittingException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import routeappjpa.Direction;
import routeappjpa.FullRoute;
import routeappjpa.Route;
import routeappjpa.User;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import routeappjpa.Coordinate_Route;

/**
 * The implementation for the RouteManagerLocal.
 *
 * @author Unai Pérez Sánchez
 */
@Stateless
public class EJBRouteManager implements RouteManagerLocal {

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    @EJB
    private CoordinateManagerLocal ejbCoordinate;

    private Logger LOGGER = Logger.getLogger("EJBRouteManager");

    /**
     * A method that inserts a route and its directions in the Database.
     *
     * @param fullRoute The route and directions.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    @Override
    public void createRoute(FullRoute fullRoute) throws CreateException {
        try {
            Route route = fullRoute.getRoute();
            Set<Direction> directions = fullRoute.getDirections();
            em.persist(route);
            LOGGER.info("Created base route");
            for (Coordinate_Route segment : fullRoute.getRoute().getCoordinates()) {
                segment.setRoute(findRoute(route.getId()));
                ejbCoordinate.createCoordinateRoute(segment);
            }
            LOGGER.info("Added segments");
            for (Direction direction : directions) {
                ejbCoordinate.createCoordinate(direction.getCoordinate());
                ejbCoordinate.createDirection(direction);
            }
            LOGGER.info("Added directions");
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }

    }

    /**
     * A method that updates a route in the Database.
     *
     * @param route The route to update.
     * @throws EdittingException An exception thrown if there was an error
     * updating the entity.
     */
    @Override
    public void updateRoute(Route route) throws EdittingException {
        try {
            em.merge(route);
            em.flush();
        } catch (Exception e) {
            throw new EdittingException(e.getMessage());
        }
    }

    /**
     * A method that removes a route in the Database.
     *
     * @param routeId The id of the route to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    @Override
    public void removeRoute(Long routeId) throws DeleteException {
        try {
            Route route = findRoute(routeId);
            for (Coordinate_Route segment : route.getCoordinates()) {
                ejbCoordinate.removeCoordinateRoute(segment);
            }

            em.remove(route);
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }

    /**
     * A method that finds a route by id in the Database.
     *
     * @param id The id of the route.
     * @return The route found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public Route findRoute(Long id) throws FindException {
        try {
            return em.find(Route.class, id);
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }

    }

    /**
     * A method that finds the all the routes in the Database.
     *
     * @return The routes found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<Route> findAllRoutes() throws FindException {
        try {
            return em.createNamedQuery("findAllRoutes").getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }

    }

    /**
     * A method that finds the routes by assigned user in the Database.
     *
     * @param userId The id of the user.
     * @return The routes found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<Route> findByAssignedUser(Long userId) throws FindException {
        try {
            return em.createNamedQuery("findByAssignedUser").setParameter("assignedTo", em.find(User.class, userId)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }

    }

}
