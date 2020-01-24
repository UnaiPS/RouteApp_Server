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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    
    @GET
    @Path("direction/type/{code}/{type}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByType(@PathParam("code") String code, @PathParam("type") String type) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        List<Direction> directions = null;
        try {
            directions = ejb.findDirectionsByType(type);
        } catch (FindException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return directions;
    }
    
    @GET
    @Path("direction/route/{code}/{route}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Direction> findDirectionsByRoute(@PathParam("code") String code, @PathParam("route") String routeId) {
        ejbSession.checkSession(code,null);
        List<Direction> directions = null;
        try {
            directions = ejb.findDirectionsByRoute(routeId);
        } catch (FindException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
            return directions;
    }
	
    
    @PUT
    @Path("direction/visited/{code}/{latitude}/{longitude}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void markDestinationVisited(@PathParam("code") String code, @PathParam("latitude") Double latitude, @PathParam("longitude") Double longitude, Coordinate_Route visited) {
        ejbSession.checkSession(code,Privilege.USER);
        try {
            ejb.updateCoordinateRoute(visited, latitude, longitude);
        } catch (UpdateException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
	
}