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
import routeappjpa.FullRoute;
import routeappjpa.Privilege;
import routeappjpa.Route;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Path("routeappjpa.route")
public class RouteFacadeREST {
    @EJB
    private RouteManagerLocal ejb;
    @EJB
    private SessionManagerLocal ejbSession;
    
    @POST
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@PathParam("code") String code, FullRoute fullRoute) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.createRoute(fullRoute);
        } catch (CreateException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("code") String code, Route route) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        Logger LOGGER = Logger
            .getLogger("service.RouteFacadeRest");
        LOGGER.warning("Server: "+route.toString());
        try {
            ejb.updateRoute(route);
        } catch (EdittingException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.removeRoute(id);
        } catch (DeleteException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Route find(@PathParam("code") String code, @PathParam("id") Long id) {
        ejbSession.checkSession(code,null);
        try {
            return ejb.findRoute(id);
        } catch (FindException ex) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findAll(@PathParam("code") String code) {
        ejbSession.checkSession(code,null);
        try {
            return ejb.findAllRoutes();
        } catch (Exception e) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        
    }

    @GET
    @Path("assignedTo/{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findByAssignedTo(@PathParam("code") String code, @PathParam("id") Long userId) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            return ejb.findByAssignedUser(userId);
        } catch (Exception e) {
            Logger.getLogger(RouteFacadeREST.class.getName()).severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        
    }
    
}
