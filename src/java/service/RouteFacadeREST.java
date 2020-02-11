package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.EdittingException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import routeappjpa.FullRoute;
import routeappjpa.Privilege;
import routeappjpa.Route;

/**
 * The REST methods of the route entity.
 *
 * @author Unai Pérez Sánchez
 */
@Path("routeappjpa.route")
public class RouteFacadeREST {

    @EJB
    private RouteManagerLocal ejb;
    @EJB
    private SessionManagerLocal ejbSession;

    private Logger LOGGER = Logger.getLogger("RouteFacadeREST");

    /**
     * A method that crates a route and all its directions.
     *
     * @param code The Session code of the client.
     * @param fullRoute The route and directions to insert.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @POST
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@PathParam("code") String code, FullRoute fullRoute) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Create route");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            ejb.createRoute(fullRoute);
            LOGGER.info("Request completed: Create route");
        } catch (CreateException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that edits a route.
     *
     * @param code The Session code of the client.
     * @param route The route to edit.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("code") String code, Route route) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Edit route");
        ejbSession.checkSession(code, null);
        try {
            ejb.updateRoute(route);
            LOGGER.info("Request completed: Edit route");
        } catch (EdittingException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("updateQuery/{route}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateByQuery(@PathParam("route") Route route){
        LOGGER.info("HTTP request received: Update Route by Query");
        try{
            ejb.updateRouteByQuery(route);
            LOGGER.info("Request completed: Update Route by Query");
        } catch (EdittingException ex){
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        
    }
    
    /**
     * A method that deletes a route.
     *
     * @param code The Session code of the client.
     * @param id The id of the route.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Delete route");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            ejb.removeRoute(id);
            LOGGER.info("Request completed: Delete route");
        } catch (DeleteException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that finds a route by id.
     *
     * @param code The Session code of the client.
     * @param id The id of the route.
     * @return The route with that id.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     */
    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Route find(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find route");
        ejbSession.checkSession(code, null);
        try {
            Route route = ejb.findRoute(id);
            LOGGER.info("Request completed: Find route");
            return route;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that finds all the routes.
     *
     * @param code The Session code of the client.
     * @return All the routes.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     */
    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findAll(@PathParam("code") String code) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find all routes");
        ejbSession.checkSession(code, null);
        try {
            List<Route> routes = ejb.findAllRoutes();
            LOGGER.info("Request completed: Find all routes");
            return routes;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }

    }

    /**
     * A method that finds all the routes assigned to a user.
     *
     * @param code The Session code of the client.
     * @param userId The id of the user.
     * @return The routes assigned to that users.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     */
    @GET
    @Path("assignedTo/{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findByAssignedTo(@PathParam("code") String code, @PathParam("id") Long userId) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find routes by assigned user");
        ejbSession.checkSession(code, null);
        try {
            List<Route> routes = ejb.findByAssignedUser(userId);
            LOGGER.info("Request completed: Find routes by assigned user");
            return routes;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }

    }

}
