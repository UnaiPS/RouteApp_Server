/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Privilege;

/**
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
    
    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Coordinate find(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find coordinate");
        ejbSession.checkSession(code,null);
        try {
            Coordinate coordinate = ejb.findCoordinate(id);
            LOGGER.info("Request completed: Find coordinate");
            return coordinate;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    @GET
    @Path("direction/type/{code}/{type}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByType(@PathParam("code") String code, @PathParam("type") String type) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find directions by type");
        ejbSession.checkSession(code,Privilege.ADMIN);
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
    
    @GET
    @Path("direction/route/{code}/{route}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByRoute(@PathParam("code") String code, @PathParam("route") String routeId) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find directions by route");
        ejbSession.checkSession(code,null);
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
	
    
    @PUT
    @Path("direction/visited/{code}/{latitude}/{longitude}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void markDestinationVisited(@PathParam("code") String code, @PathParam("latitude") Double latitude, @PathParam("longitude") Double longitude, Coordinate_Route visited) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Mark destination as visited");
        ejbSession.checkSession(code,Privilege.USER);
        try {
            ejb.updateCoordinateRoute(visited, latitude, longitude);
            LOGGER.info("Request completed: Mark destination as visited");
        } catch (UpdateException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
	
}
