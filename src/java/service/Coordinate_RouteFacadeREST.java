/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import routeappjpa.Coordinate_Route;
import routeappjpa.Coordinate_RouteId;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Stateless
@Path("routeappjpa.coordinate_route")
public class Coordinate_RouteFacadeREST extends AbstractFacade<Coordinate_Route> {

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;

    private Coordinate_RouteId getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;routeId=routeIdValue;coordinateId=coordinateIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        routeappjpa.Coordinate_RouteId key = new routeappjpa.Coordinate_RouteId();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> routeId = map.get("routeId");
        if (routeId != null && !routeId.isEmpty()) {
            key.setRouteId(new java.lang.Long(routeId.get(0)));
        }
        java.util.List<String> coordinateId = map.get("coordinateId");
        if (coordinateId != null && !coordinateId.isEmpty()) {
            key.setCoordinateId(new java.lang.Long(coordinateId.get(0)));
        }
        return key;
    }

    public Coordinate_RouteFacadeREST() {
        super(Coordinate_Route.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Coordinate_Route entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Coordinate_Route entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        routeappjpa.Coordinate_RouteId key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Coordinate_Route find(@PathParam("id") PathSegment id) {
        routeappjpa.Coordinate_RouteId key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Coordinate_Route> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Coordinate_Route> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
