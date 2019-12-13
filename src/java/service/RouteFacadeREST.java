/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.EdittingException;
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
import routeappjpa.Route;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Path("routeappjpa.route")
public class RouteFacadeREST {
    @EJB
    private RouteManagerLocal ejb;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Route route) {
        try {
            ejb.createRoute(route);
        } catch (CreateException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Route route) {
        try {
            ejb.updateRoute(route);
        } catch (EdittingException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            ejb.removeRoute(id);
        } catch (DeleteException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Route find(@PathParam("id") Long id) {
        try {
            return ejb.findRoute(id);
        } catch (FindException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Route> findAll() {
        try {
            return ejb.findAllRoutes();
        } catch (Exception e) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        
    }

    @GET
    @Path("assignedTo/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Route> findByAssignedTo(@PathParam("id") Long userId) {
        try {
            return ejb.findByAssignedUser(userId);
        } catch (Exception e) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        
    }
    
}
