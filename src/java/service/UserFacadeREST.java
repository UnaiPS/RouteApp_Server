/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.BadPasswordException;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.EmailException;
import exceptions.IncorrectPasswdException;
import exceptions.UserNotFoundException;
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
import messages.UserPasswd;
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
    public void edit(@PathParam("id") Long id, User entity) throws UserNotFoundException{
        try {
            ejb.editUser(entity);
        } catch (EdittingException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
    }
    
    
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    public User login(User user) throws BadPasswordException, UserNotFoundException {
                return ejb.login(user);
    }


    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            ejb.removeUser(id);
        } catch (DeleteException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") Long id) {
        try {
            return ejb.find(id);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
            return null;
        }
    }
    
    @GET
    @Path("{id}/{fullName}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") Long id, @PathParam("fullName") String fullName) {
    return ejb.prueba(id, fullName);

    }
    
    
    @GET
    @Path("forgottenpasswd/{email}")
    @Produces({MediaType.APPLICATION_XML})
    public int forgottenpasswd(@PathParam("email") String email) {
        try {
            return ejb.forgottenpasswd(email);
        } catch (EmailException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
        return 1;
    }
    
    
    
    @PUT
    @Path("editPasswd")
    @Produces({MediaType.APPLICATION_XML})
    public User editPasswd(UserPasswd u) {
        try {
            return ejb.editPasswd(u);
        } catch (IncorrectPasswdException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        } catch (EdittingException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
        return null;
    }
    
    @GET
    @Path("login/{login}")
    @Produces({MediaType.APPLICATION_XML})
    public User findAccountByLogin(@PathParam("login") String login) {
        try {
            return ejb.findAccountByLogin(login);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
            return null;
        }
    }
    
    @GET
    @Path("deliveryAccounts")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAllDeliveryAccounts() {
            return ejb.findAllDeliveryAccounts();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() {
        return ejb.findAll();
    }
   
}
