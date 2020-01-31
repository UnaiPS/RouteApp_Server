package service;

import exceptions.FindException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import routeappjpa.Coordinate;
import routeappjpa.Direction;
import routeappjpa.Privilege;

/**
 * The REST methods of the coordinate entity.
 *
 * @author Jon Calvo Gaminde
 */
@Path("routeappjpa.coordinate")
public class CoordinateFacadeREST {

    @EJB
    private CoordinateManagerLocal ejb;
    @EJB
    private SessionManagerLocal ejbSession;

    private Logger LOGGER = Logger.getLogger("CoordinateFacadeREST");

    /**
     * A method that finds a coordinate by id.
     *
     * @param code The Session code of the client.
     * @param id The id of the coordinate.
     * @return The coordinate with that id.
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
    public Coordinate find(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find coordinate");
        ejbSession.checkSession(code, null);
        try {
            Coordinate coordinate = ejb.findCoordinate(id);
            LOGGER.info("Request completed: Find coordinate");
            return coordinate;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that finds all the directions by coordinate type.
     *
     * @param code The Session code of the client.
     * @param type The type of the directions.
     * @return The directions with that type.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @GET
    @Path("direction/type/{code}/{type}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByType(@PathParam("code") String code, @PathParam("type") String type) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find directions by type");
        ejbSession.checkSession(code, Privilege.ADMIN);
        List<Direction> directions = null;
        try {
            directions = ejb.findDirectionsByType(type);
            LOGGER.info("Request completed: Find directions by type");
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return directions;
    }

    /**
     * A method that finds all the directions by route.
     *
     * @param code The Session code of the client.
     * @param routeId The id of the route.
     * @return The directions of that route.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     */
    @GET
    @Path("direction/route/{code}/{route}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByRoute(@PathParam("code") String code, @PathParam("route") String routeId) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find directions by route");
        ejbSession.checkSession(code, null);
        List<Direction> directions = null;
        try {
            directions = ejb.findDirectionsByRoute(routeId);
            LOGGER.info("Request completed: Find directions by route");
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return directions;
    }

    /**
     * A method that marks a coordinate_route visited with an specific GPS
     * coordinate.
     *
     * @param code The Session code of the client.
     * @param routeId The route id of the coordinate_route.
     * @param coordinateId The route id of the coordinate_route.
     * @param gps The gps coordinate.
     * @return The new id of the GPS coordinate.
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
    @Path("direction/visited/{code}/{routeId}/{coordinateId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Long markDestinationVisited(@PathParam("code") String code, @PathParam("routeId") Long routeId, @PathParam("coordinateId") Long coordinateId, Coordinate gps) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Mark destination as visited");
        ejbSession.checkSession(code, Privilege.USER);
        try {
            Long id;
            id = ejb.updateCoordinateRoute(gps, routeId, coordinateId);
            LOGGER.info("Request completed: Mark destination as visited");
            return id;
        } catch (UpdateException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

}
