/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.BadPasswordException;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DoesntMatchException;
import exceptions.EdittingException;
import exceptions.EmailException;
import exceptions.UserNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
 *
 * @author Daira Eguzkiza
 */
@Path("routeappjpa.user")
public class UserFacadeREST {

    @EJB
    private EJBUserLocal ejb;
    @EJB
    private SessionManagerLocal ejbSession;
    
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
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("code") String code, User entity) throws UserNotFoundException{
        ejbSession.checkSession(code,null);
        try {
            ejb.editUser(entity);
        } catch (EdittingException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
    }
    
    
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Session login(User user) throws BadPasswordException, UserNotFoundException {
        return ejb.login(user);
    }


    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.removeUser(id);
        } catch (DeleteException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        }
    }

    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("code") String code, @PathParam("id") Long id) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            return ejb.find(id);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
            return null;
        }
    }
    
    
    
    @GET
    @Path("forgottenpasswd/{email}/{login}")
    @Produces({MediaType.APPLICATION_XML})
    public void forgottenpasswd(@PathParam("email") String email, @PathParam("login") String login) {
        try {
            ejb.forgottenpasswd(email, login);
        } catch (EmailException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
        } catch (DoesntMatchException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @GET
    @Path("login/{code}/{login}")
    @Produces({MediaType.APPLICATION_XML})
    public User findAccountByLogin(@PathParam("code") String code, @PathParam("login") String login) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            return ejb.findAccountByLogin(login);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
            return null;
        }
    }
    
    @GET
    @Path("privilege/{code}/{privilege}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findByPrivilege(@PathParam("code") String code, @PathParam("privilege") String privilege) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        List<User> users = ejb.findByPrivilege(privilege);
        return users;
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll(@PathParam("code") String code) {
        ejbSession.checkSession(code,Privilege.ADMIN);
        return ejb.findAll();
    }
    
    @POST
    @Path("email/{code}")
    @Produces({MediaType.APPLICATION_XML})
    public String emailConfirmation(@PathParam("code") String code, User entity) {
        ejbSession.checkSession(code,null);
        try {
            return ejb.emailConfirmation(entity);
        } catch (Exception ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).severe(ex.getMessage());
            return null;
        }
    }
   
}
