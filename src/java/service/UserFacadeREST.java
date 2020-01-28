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
import exceptions.FindException;
import exceptions.UserNotFoundException;
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
import javax.ws.rs.NotFoundException;
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
    
    private Logger LOGGER = Logger.getLogger("UserFacadeREST");
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(User entity) throws InternalServerErrorException{
        LOGGER.info("HTTP request received: Create user");
        try {
            ejb.createUser(entity);
            LOGGER.info("Request completed: Create user");
        } catch (CreateException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("code") String code, User entity) throws InternalServerErrorException, NotAuthorizedException, BadRequestException{
        LOGGER.info("HTTP request received: Edit user");
        ejbSession.checkSession(code,null);
        try {
            ejb.editUser(entity);
            LOGGER.info("Request completed: Edit user");
        } catch (EdittingException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
    
    
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Session login(User user) throws NotFoundException, NotAuthorizedException {
        LOGGER.info("HTTP request received: Login");
        try {
            Session session = ejb.login(user);
            LOGGER.info("Request completed: Login");
            return session;
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (BadPasswordException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotAuthorizedException(ex.getMessage());
        }
    }


    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id)throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Delete user");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            ejb.removeUser(id);
            LOGGER.info("Request completed: Delete user");
        } catch (DeleteException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User find(@PathParam("code") String code, @PathParam("id") Long id) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find user");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            User user = ejb.find(id);
            LOGGER.info("Request completed: Find user");
            return user;
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }
    
    
    
    @GET
    @Path("forgottenpasswd/{email}/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void forgottenpasswd(@PathParam("email") String email, @PathParam("login") String login) throws InternalServerErrorException {
        LOGGER.info("HTTP request received: Restore password");
        try {
            ejb.forgottenpasswd(email, login);
            LOGGER.info("Request completed: Restore password");
        } catch (EmailException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (DoesntMatchException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
    
    
    
    @GET
    @Path("login/{code}/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User findAccountByLogin(@PathParam("code") String code, @PathParam("login") String login) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find user by login");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            User user = ejb.findAccountByLogin(login);
            LOGGER.info("Request completed: Find user by login");
            return user;
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }
    
    @GET
    @Path("privilege/{code}/{privilege}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findByPrivilege(@PathParam("code") String code, @PathParam("privilege") String privilege) throws NotAuthorizedException, BadRequestException, ForbiddenException{
        LOGGER.info("HTTP request received: Find users by privilege");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            List<User> users = ejb.findByPrivilege(privilege);
            LOGGER.info("Request completed: Find users by privilege");
            return users;
        }catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll(@PathParam("code") String code) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find all users");
        ejbSession.checkSession(code,Privilege.ADMIN);
        try {
            List<User> users = ejb.findAll();
            LOGGER.info("Request completed: Find all users");
            return users;
        }catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }
    
    @POST
    @Path("email/{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String emailConfirmation(@PathParam("code") String code, User entity) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Email confirmation");
        ejbSession.checkSession(code,null);
        try {
            String confirmation = ejb.emailConfirmation(entity);
            LOGGER.info("Request completed: Email confirmation");
            return confirmation;
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
   
}
