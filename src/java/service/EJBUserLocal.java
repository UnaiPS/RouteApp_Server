package service;

import exceptions.BadPasswordException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.DoesntMatchException;
import exceptions.EmailException;
import exceptions.FindException;
import exceptions.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;
import javax.ws.rs.ForbiddenException;
import routeappjpa.Session;
import routeappjpa.User;

/**
 * The interface for the EJB of the User entity.
 *
 * @author Daira Eguzkiza
 */
@Local
public interface EJBUserLocal {

    /**
     * A method that inserts a user in the Database.
     *
     * @param user The user to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    public void createUser(User user) throws CreateException;

    /**
     * A method that updates a user in the Database.
     *
     * @param user The user to update.
     * @throws EdittingException An exception thrown if there was an error
     * updating the entity.
     */
    public void editUser(User user) throws EdittingException;

    /**
     * A method that removes a user in the Database.
     *
     * @param id The id of the user.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    public void removeUser(Long id) throws DeleteException;

    /**
     * A method that finds a user by id in the Database.
     *
     * @param id The id of the user.
     * @return The user found.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    public User find(Long id) throws UserNotFoundException;

    /**
     * A method that finds a user by login in the Database.
     *
     * @param login The login of the user.
     * @return The found user.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    public User findAccountByLogin(String login) throws UserNotFoundException;

    /**
     * A method that finds the users by privilege in the Database.
     *
     * @param privilege The privilege to find.
     * @return The users found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<User> findByPrivilege(String privilege) throws FindException;

    /**
     * A method that finds the all the users in the Database.
     *
     * @return The users found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<User> findAll() throws FindException;

    /**
     * A method that sends the user a new password to the email, changing it in
     * the Database.
     *
     * @param email The email of the user.
     * @param login The login of the user.
     * @throws EmailException An exception thrown if there was an error sending
     * the email.
     * @throws DoesntMatchException An exception thrown if the login and email
     * are wrong.
     * @throws ForbiddenException An exception thrown if the password was
     * changed recently.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException, ForbiddenException, UserNotFoundException;

    /**
     * A method that checks the login and password of a user and returns its
     * session and user data.
     *
     * @param user The user login and password.
     * @return The session with the user data.
     * @throws BadPasswordException An exception thrown if the password is
     * wrong.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    public Session login(User user) throws BadPasswordException, UserNotFoundException;

    /**
     * A method that send the user a code to the email and returns it hashed to
     * the client for comparation.
     *
     * @param user The user who needs the code.
     * @return The hashed code.
     * @throws EmailException An exception thrown if there was an error sending
     * the email.
     */
    public String emailConfirmation(User user) throws EmailException;
    public void changeName(String fullName, long id) throws EdittingException;
}
