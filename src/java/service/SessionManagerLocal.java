package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
 * The interface for the EJB of the Session entity.
 *
 * @author Jon Calvo Gaminde
 */
public interface SessionManagerLocal {

    /**
     * A method that finds or creates the session for a client and returns it.
     *
     * @param user The user of the session.
     * @return A valid session for the user.
     * @throws CreateException An exception thrown if there was an error
     * creating a new session.
     * @throws DeleteException An exception thrown if there was an error
     * deleting the old session.
     */
    public Session getSession(User user) throws CreateException, DeleteException;

    /**
     * A method that checks if the session code is valid and returns the user.
     *
     * @param encryptSession The encrypted session code.
     * @param requiredPrivilege The requiered privilege for the request, or null
     * if any.
     * @return The user of the valid session.
     * @throws InternalServerErrorException An exception thrown if there was an
     * error checking the session.
     * @throws NotAuthorizedException An exception thrown if the session is
     * invalid.
     * @throws ForbiddenException An exception thrown if the user has the wrong
     * privilege.
     * @throws BadRequestException An exception thrown if the code was
     * malformed.
     */
    public User checkSession(String encryptSession, Privilege requiredPrivilege) throws InternalServerErrorException, NotAuthorizedException, ForbiddenException, BadRequestException;
}
