/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import routeappjpa.User;

/**
 *
 * @author Daira Eguzkiza
 */
@Stateless
@Path("routeappjpa.user")
public class UserFacadeREST {

    @EJB
    private EJBUserLocal ejb;
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) {
        try {
            ejb.createUser(entity);
        } catch (CreateException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, User entity) {
        ejb.editUser(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        ejb.removeUser(ejb.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") Long id) {
        return ejb.find(id);
    }
    
    @GET
    @Path("{login}")
    @Produces({MediaType.APPLICATION_XML})
    public User findAccountByLogin(@PathParam("login") String login) {
        return ejb.findAccountByLogin(login);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() {
        return ejb.findAll();
    }
/*
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
*/
   
}
