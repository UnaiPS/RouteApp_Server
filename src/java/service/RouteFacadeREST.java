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
    
    @POST
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@PathParam("code") String code, FullRoute fullRoute) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Create route");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.createRoute(fullRoute);
            LOGGER.info("Request completed: Create route");
        } catch (CreateException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("code") String code, Route route) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Edit route");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.updateRoute(route);
            LOGGER.info("Request completed: Edit route");
        } catch (EdittingException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Delete route");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.removeRoute(id);
            LOGGER.info("Request completed: Delete route");
        } catch (DeleteException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Route find(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find route");
        ejbSession.checkSession(code,null);
        try {
            Route route = ejb.findRoute(id);
            LOGGER.info("Request completed: Find route");
            return route;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findAll(@PathParam("code") String code)  throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Find all routes");
        ejbSession.checkSession(code,null);
        try {
            List<Route> routes = ejb.findAllRoutes();
            LOGGER.info("Request completed: Find all routes");
            return routes;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
        
    }

    @GET
    @Path("assignedTo/{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Route> findByAssignedTo(@PathParam("code") String code, @PathParam("id") Long userId) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find routes by assigned user");
        ejbSession.checkSession(code,null);
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
