package service;

import encryption.Decrypt;
import encryption.Hasher;
import exceptions.BadPasswordException;
import exceptions.EmailException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.DoesntMatchException;
import exceptions.FindException;
import exceptions.UserNotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ForbiddenException;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
 * The implementation for the EJBUserLocal.
 *
 * @author Daira Eguzkiza
 */
@Stateless
public class EJBUser<T> implements EJBUserLocal {

    private Class<T> entityClass;

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    @EJB
    private SessionManagerLocal ejbSession;

    private Logger LOGGER = Logger.getLogger("EJBUser");

    ResourceBundle properties = ResourceBundle.getBundle("service.serverconfig");
    private final String HOST = properties.getString("smtpHost");
    private final String PORT = properties.getString("smtpPort");
    private final long HOURS = Long.parseLong(properties.getString("restorePasswordCooldownHours"));

    /**
     * A method that inserts a user in the Database.
     *
     * @param user The user to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    @Override
    public void createUser(User user) throws CreateException {
        try {
            user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
            em.persist(user);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    @Override
    public void changeName(String fullName, long id) throws EdittingException {
        try {
            em.createNamedQuery("modificacion").setParameter("fullName", fullName).setParameter("id", id).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new EdittingException(e.getMessage());
        }
    }
    
    /**
     * A method that updates a user in the Database.
     *
     * @param user The user to update.
     * @throws EdittingException An exception thrown if there was an error
     * updating the entity.
     */
    @Override
    public void editUser(User user) throws EdittingException {
        try {
            if (find(user.getId()).getLastPasswordChange().compareTo(user.getLastPasswordChange()) < 0) {
                user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
                LOGGER.info("User password was changed");
            }
            em.merge(user);
            em.flush();
        } catch (Exception e) {
            throw new EdittingException(e.getMessage());
        }
    }

    /**
     * A method that removes a user in the Database.
     *
     * @param id The id of the user.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    @Override
    public void removeUser(Long id) throws DeleteException {
        User u = new User();
        u = em.find(User.class, id);
        try {
            em.remove(em.merge(u));
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());

        }
    }

    /**
     * A method that finds a user by id in the Database.
     *
     * @param id The id of the user.
     * @return The user found.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    @Override
    public User find(Long id) throws UserNotFoundException {
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

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
    @Override
    public Session login(User user) throws BadPasswordException, UserNotFoundException {
        User u = new User();
        try {
            u = (User) em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
            if (Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())).equals(u.getPassword())) {
                u.setLastAccess(new Date());
                return ejbSession.getSession(u);
            } else {
                throw new BadPasswordException("Password is wrong.");
            }
        } catch (BadPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    /**
     * A method that finds a user by login in the Database.
     *
     * @param login The login of the user.
     * @return The found user.
     * @throws UserNotFoundException An exception thrown if no user was found.
     */
    @Override
    public User findAccountByLogin(String login) throws UserNotFoundException {
        try {
            return (User) em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    /**
     * A method that finds the users by privilege in the Database.
     *
     * @param privilege The privilege to find.
     * @return The users found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<User> findByPrivilege(String privilege) throws FindException {
        try {
            return em.createNamedQuery("findByPrivilege").setParameter("privilege", Privilege.valueOf(privilege)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
    }

    /**
     * A method that finds the all the users in the Database.
     *
     * @return The users found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    @Override
    public List<User> findAll() throws FindException {
        try {
            return em.createNamedQuery("findAll").getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
    }

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
    @Override
    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException, ForbiddenException, UserNotFoundException {
        try {

            User user = (User) em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
            if (user.getEmail().equals(email)) {
                if (user.getLastPasswordChange().getTime() + (HOURS * 3600000) < Date.from(Instant.now()).getTime()) {
                    EmailSender es = new EmailSender(HOST, PORT);
                    String nuevaContra = createCode(10);

                    user.setPassword(Hasher.encrypt(nuevaContra));
                    editUser(user);
                    es.sendEmail(email, nuevaContra, 0);
                    LOGGER.info("Password restoration email sended");
                } else {
                    throw new ForbiddenException("The password was restored or changed in the last " + HOURS + " hours. Restore denegated.");
                }
            } else {
                throw new DoesntMatchException("The login does not match with the email.");
            }

        } catch (DoesntMatchException | ForbiddenException ex) {
            throw ex;
        } catch (NoResultException ex) {
            throw new UserNotFoundException("No user found with login: " + login);
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
    }

    /**
     * A method that send the user a code to the email and returns it hashed to
     * the client for comparation.
     *
     * @param user The user who needs the code.
     * @return The hashed code.
     * @throws EmailException An exception thrown if there was an error sending
     * the email.
     */
    @Override
    public String emailConfirmation(User user) throws EmailException {
        String code = createCode(4);
        try {
            EmailSender es = new EmailSender(HOST, PORT);
            es.sendEmail(user.getEmail(), code, 1);
            LOGGER.info("Identity confirmation email sended");
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
        return Hasher.encrypt(code);
    }

    /**
     * A method that creates a random code mixing lowercase and uppercase
     * letters with numbers.
     *
     * @param length The legth of the code.
     * @return The random code.
     */
    private static String createCode(int length) {
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
