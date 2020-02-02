package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.EdittingException;
import java.util.List;
import javax.ejb.Local;
import routeappjpa.FullRoute;
import routeappjpa.Route;

/**
 * The interface for the EJB of the Route entity.
 *
 * @author Unai Pérez Sánchez
 */
@Local
public interface RouteManagerLocal {

    /**
     * A method that inserts a route and its directions in the Database.
     *
     * @param fullRoute The route and directions.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    public void createRoute(FullRoute fullRoute) throws CreateException;

    /**
     * A method that updates a route in the Database.
     *
     * @param route The route to update.
     * @throws EdittingException An exception thrown if there was an error
     * updating the entity.
     */
    public void updateRoute(Route route) throws EdittingException;

    /**
     * A method that removes a route in the Database.
     *
     * @param routeId The id of the route to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    public void removeRoute(Long routeId) throws DeleteException;

    /**
     * A method that finds a route by id in the Database.
     *
     * @param id The id of the route.
     * @return The route found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public Route findRoute(Long id) throws FindException;

    /**
     * A method that finds the all the routes in the Database.
     *
     * @return The routes found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<Route> findAllRoutes() throws FindException;

    /**
     * A method that finds the routes by assigned user in the Database.
     *
     * @param userId The id of the user.
     * @return The routes found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<Route> findByAssignedUser(Long userId) throws FindException;
}
