package service;

import encryption.Decrypt;
import exceptions.CreateException;
import exceptions.DeleteException;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ForbiddenException;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
 * The implementation for the SessionMmanagerLocal.
 *
 * @author Jon Calvo Gaminde
 */
@Stateless
public class EJBSessionManager implements SessionManagerLocal {

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    private ResourceBundle properties = ResourceBundle.getBundle("service.serverconfig");
    private final int MINUTES = Integer.parseInt(properties.getString("sessionMaxInactivityMinutes"));
    private Logger LOGGER = Logger.getLogger("EJBSessionManager");

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
    @Override
    public Session getSession(User user) throws CreateException, DeleteException {
        LOGGER.info("Preparing the session");
        Session session = null;
        try {
            session = (Session) em.createNamedQuery("getSessionCode")
                    .setParameter("logged", user).getSingleResult();
            // If the session is old, we need a new one.
            if (session.getLastAction().getTime() + MINUTES * 60000 < Instant.now().toEpochMilli()) {
                try {
                    // Delete the old session.
                    em.remove(em.merge(session));
                } catch (Exception e) {
                    throw new DeleteException(e.getMessage());
                }
                // Ask for a new one.
                throw new NoResultException("The user has been inactive for " + MINUTES + " minutes");
            } else {
                session.setLastAction(Date.from(Instant.now()));
                em.merge(session);
                em.flush();
            }
        } catch (NoResultException e) {
            LOGGER.info(e.getLocalizedMessage() + ". Creating a new Session.");
            // New session is created.
            session = new Session();
            session.setLogged(user);
            session.setCode(createCode(6));
            session.setLastAction(Date.from(Instant.now()));
            em.persist(session);
        } catch (DeleteException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
        LOGGER.info("Session prepared");
        // Return the found or new session.
        return session;
    }

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
    @Override
    public User checkSession(String encryptSession, Privilege requiredPrivilege) throws InternalServerErrorException, NotAuthorizedException, ForbiddenException, BadRequestException {
        LOGGER.info("Checking session");
        User user = null;
        String code = null;
        Long millis = null;
        try {
            try {
                // We need the two parts of the encrypted session, the session code and the time mark.
                encryptSession = Decrypt.descifrarTexto(encryptSession);
                code = encryptSession.substring(0, 6);
                millis = Long.parseLong(encryptSession.substring(6));
            } catch (Exception ex) {
                throw new BadRequestException("Malformed session code.");
            }
            // Check if the session exists.
            Session session = (Session) em.createNamedQuery("findSessionByCode")
                    .setParameter("code", code).getSingleResult();
            // We give the times a little error margin, and check if the time mark is possible,
            // and the privilege match.
            long lastAction = session.getLastAction().getTime() - 15000L;
            if (lastAction > millis || millis > Instant.now().toEpochMilli() + 15000L) {
                throw new NoResultException("Invalid code.");
            } else if (lastAction + MINUTES * 60000 < Instant.now().toEpochMilli()) {
                throw new NoResultException("User has been inactive for too long.");
            } else if (requiredPrivilege != null && !session.getLogged().getPrivilege().equals(requiredPrivilege)) {
                throw new ForbiddenException("Wrong user privilege.");
            } else {
                // The session is valid, update the last action.
                user = session.getLogged();
                session.setLastAction(Date.from(Instant.now()));
                em.merge(session);
                em.flush();
            }
        } catch (NoResultException e) {
            LOGGER.severe("HTTP request denied. Reason: " + e.getMessage());
            // The user was not logged or the session was too old.
            throw new NotAuthorizedException(e.getMessage());
        } catch (ForbiddenException | BadRequestException e) {
            LOGGER.severe("HTTP request denied. Reason: " + e.getMessage());
            // The privilege level was not meet or the code was malformed.
            throw e;
        } catch (Exception e) {
            // Some error happened in the checking.
            throw new InternalServerErrorException(e.getMessage());
        }
        LOGGER.info("The session is valid");
        return user;
    }

    /**
     * A method that creates a random code mixing lowercase and uppercase
     * letters with numbers.
     *
     * @param length The legth of the code.
     * @return The random code.
     */
    private String createCode(int length) {
        String code = "";
        for (int i = 0; i < length; i++) {
            // 50% chance of letter, 50% chance of number
            if (Math.random() < 0.5) {
                // Letter
                int num = (int) Math.floor(Math.random() * (91 - 65) + 65);
                // 50% chance of lowercase, 50% chance of uppercase
                if (Math.random() < 0.5) {
                    // Lowercase
                    num += 32;
                }
                // Else uppercase
                char letter = (char) num;
                code = code + letter;
            } else {
                // Number
                int num = (int) Math.floor(Math.random() * 10);
                code = code + num;
            }
        }
        return code;
    }

}
