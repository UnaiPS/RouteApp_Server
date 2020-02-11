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
import java.util.logging.Level;
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
 * The REST methods of the user entity.
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
    public void create(User entity) throws InternalServerErrorException {
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
    @Path("changeName/{fullName}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void changeName(@PathParam("fullName") String fullName, @PathParam("id") long id) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        try {
            LOGGER.info("HTTP request received: change user's full name");
            ejb.changeName(fullName, id);
            LOGGER.info("Request completed: updated user's full name");
        } catch (EdittingException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * A method that edits a user.
     *
     * @param code The Session code of the client.
     * @param entity The user to edit.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     */
    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("code") String code, User entity) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Edit user");
        ejbSession.checkSession(code, null);
        try {
            ejb.editUser(entity);
            LOGGER.info("Request completed: Edit user");
        } catch (EdittingException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that checks the login and password of a user and returns its
     * session and user data.
     *
     * @param user The user with the login and password
     * @return The session of the user, and its data.
     * @throws NotFoundException An exception thrown if the login was not found.
     * @throws NotAuthorizedException An exception thrown if the password is
     * wrong.
     */
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

    /**
     * A method that deletes a user.
     *
     * @param code The Session code of the client.
     * @param id The id of the user.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @DELETE
    @Path("{code}/{id}")
    public void remove(@PathParam("code") String code, @PathParam("id") Long id) throws InternalServerErrorException, NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Delete user");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            ejb.removeUser(id);
            LOGGER.info("Request completed: Delete user");
        } catch (DeleteException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * A method that finds a user by id.
     *
     * @param code The Session code of the client.
     * @param id The id of the user.
     * @return The user with that id.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @GET
    @Path("{code}/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User find(@PathParam("code") String code, @PathParam("id") Long id) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find user");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            User user = ejb.find(id);
            LOGGER.info("Request completed: Find user");
            return user;
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * A method that sends the user a new password to the email.
     *
     * @param email The email of the user.
     * @param login The login of the user.
     * @throws InternalServerErrorException An exception thrown if the request
     * was unsuccessful.
     * @throws ForbiddenException An exception thrown if the password was
     * changed recently.
     * @throws NotAuthorizedException An exception thrown if the email and the
     * login do not match.
     * @throws NotFoundException An exception thrown if the user was not found.
     */
    @GET
    @Path("forgottenpasswd/{email}/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void forgottenpasswd(@PathParam("email") String email, @PathParam("login") String login) throws InternalServerErrorException, ForbiddenException, NotAuthorizedException, NotFoundException {
        LOGGER.info("HTTP request received: Restore password");
        try {
            ejb.forgottenpasswd(email, login);
            LOGGER.info("Request completed: Restore password");
        } catch (EmailException ex) {
            LOGGER.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        } catch (DoesntMatchException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotAuthorizedException(ex.getMessage());
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        } catch (ForbiddenException ex) {
            LOGGER.severe(ex.getMessage());
            throw ex;
        }
    }

    /**
     * A method that finds a user by login.
     *
     * @param code The Session code of the client.
     * @param login The login of the user.
     * @return The user with that login.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @GET
    @Path("login/{code}/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User findAccountByLogin(@PathParam("code") String code, @PathParam("login") String login) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find user by login");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            User user = ejb.findAccountByLogin(login);
            LOGGER.info("Request completed: Find user by login");
            return user;
        } catch (UserNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * A method that finds all the users with a specific privilege level.
     *
     * @param code The Session code of the client.
     * @param privilege The privilege of the users.
     * @return The users with that privilege level.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @GET
    @Path("privilege/{code}/{privilege}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findByPrivilege(@PathParam("code") String code, @PathParam("privilege") String privilege) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find users by privilege");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            List<User> users = ejb.findByPrivilege(privilege);
            LOGGER.info("Request completed: Find users by privilege");
            return users;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * A method that finds all the users.
     *
     * @param code The Session code of the client.
     * @return All the users.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll(@PathParam("code") String code) throws NotAuthorizedException, BadRequestException, ForbiddenException {
        LOGGER.info("HTTP request received: Find all users");
        ejbSession.checkSession(code, Privilege.ADMIN);
        try {
            List<User> users = ejb.findAll();
            LOGGER.info("Request completed: Find all users");
            return users;
        } catch (FindException ex) {
            LOGGER.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * A method that send the user a code to the email and returns it hashed to
     * the client for comparation.
     *
     * @param code The Session code of the client.
     * @param entity The user who needs the code.
     * @return The hashed code.
     * @throws NotAuthorizedException An exception thrown if the code was
     * invalid.
     * @throws BadRequestException An exception thrown if the request was
     * malformed.
     * @throws ForbiddenException An exception thrown if the client has the
     * wrong privilege.
     */
    @POST
    @Path("email/{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String emailConfirmation(@PathParam("code") String code, User entity) throws InternalServerErrorException, NotAuthorizedException, BadRequestException {
        LOGGER.info("HTTP request received: Email confirmation");
        ejbSession.checkSession(code, null);
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
