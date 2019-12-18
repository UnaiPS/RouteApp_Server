/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.UpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Luggage;
import routeappjpa.Type;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Path("routeappjpa.coordinate")
public class CoordinateFacadeREST {
    @EJB
    private CoordinateManagerLocal ejb;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createWaypoint(Coordinate coordinate) {
        try {
            ejb.createCoordinate(coordinate);
        } catch (CreateException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Coordinate coordinate) {
        try {
            ejb.updateCoordinate(coordinate);
        } catch (UpdateException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            ejb.removeCoordinate(id);
        } catch (DeleteException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Coordinate find(@PathParam("id") Long id) {
        try {
            return ejb.findCoordinate(id);
        } catch (FindException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("type/{type}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Coordinate> findByType(@PathParam("type") String type) {
        List<Coordinate> coords=null;
        try {
            coords = ejb.findByType(type);
        } catch (Exception e) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        return coords;
    }
    @POST
    @Path("direction")
    @Consumes({MediaType.APPLICATION_XML})
    public void createDirection(Direction direction) {
        try {
            ejb.createDirection(direction);
        } catch (CreateException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    @PUT
    @Path("direction/visited")
    @Consumes({MediaType.APPLICATION_XML})
    public void markDestinationVisited(Luggage luggage) {
        try {
            ArrayList<Object> content = luggage.getLuggage();
            Coordinate_Route visited = (Coordinate_Route) content.get(0);
            Coordinate gps = (Coordinate) content.get(1);
            ejb.createCoordinate(gps);
            visited.setVisited(ejb.getIdByData(gps));
            ejb.updateCoordinateRoute(visited);
        } catch (CreateException | FindException | UpdateException ex) {
            Logger.getLogger(CoordinateFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
}