/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
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


    @Override
    public void createUser(User user) throws CreateException {
        try{
            user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
            em.persist(user);
        }catch(Exception e){
                throw new CreateException(e.getMessage());
        }
    }
    
    @Override
    public void editUser(User user) throws EdittingException {
        try{
            if (find(user.getId()).getLastPasswordChange().compareTo(user.getLastPasswordChange())<0) {
                user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
                LOGGER.info("User password was changed");
            }
            em.merge(user);
            em.flush();
        }catch(Exception e){
            throw new EdittingException(e.getMessage());
        }
    }

    @Override
    public void removeUser(Long id) throws DeleteException{
        User u = new User();
            u = em.find(User.class, id);
        try{em.remove(em.merge(u));
        }catch(Exception e){
            throw new DeleteException(e.getMessage());

        }
    }

    @Override
    public User find(Long id) throws UserNotFoundException{
        try{
            return em.find(User.class, id);
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }
    
    
    @Override
    public Session login(User user) throws BadPasswordException, UserNotFoundException{
        User u = new User();
        try{
            u = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
            if(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())).equals(u.getPassword())){
                u.setLastAccess(new Date());
                return ejbSession.getSession(u);
            }else{
                throw new BadPasswordException("Password is wrong.");
            }
        }catch(BadPasswordException e){
            throw e;
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @Override
    public User findAccountByLogin(String login) throws UserNotFoundException {
        try {
            return (User)em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }
    @Override
    public List<User> findByPrivilege(String privilege) throws FindException {
        try {
            return em.createNamedQuery("findByPrivilege").setParameter("privilege",Privilege.valueOf(privilege)).getResultList();
        }catch(Exception e){
            throw new FindException(e.getMessage());
        }
    }
    
    @Override
    public List<User> findAll() throws FindException {
        try {
            return em.createNamedQuery("findAll").getResultList();
        }catch(Exception e){
            throw new FindException(e.getMessage());
        }
    }
        
    
    @Override
    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException{
        try {
            
            User user = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
            if(user.getEmail().equals(email)){
                EmailSender es = new EmailSender(HOST, PORT);
                String nuevaContra = createCode(10);
                
                user.setPassword(Hasher.encrypt(nuevaContra));
                editUser(user);
                es.sendEmail(email, nuevaContra, 0);
                LOGGER.info("Password restoration email sended");
            }else{
                throw new DoesntMatchException("The login doesn't match with the email.");
            }
           
        } catch (DoesntMatchException ex) {
            throw ex;  
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
    }
    
    @Override
    public String emailConfirmation(User user) throws EmailException {
        String code = createCode(4);
        try{
            EmailSender es = new EmailSender(HOST, PORT);           
            es.sendEmail(user.getEmail(), code, 1);
            LOGGER.info("Identity confirmation email sended");
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
        return Hasher.encrypt(code);
    }
    
    
    private static String createCode(int length) {
        String code = "";
        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.5) {
                int num = (int) Math.floor(Math.random()*(91-65)+65);
                if (Math.random() < 0.5) {
                    num += 32;
                }
                char letter = (char) num;
                code = code + letter;
            } else {
                int num = (int) Math.floor(Math.random()*10);
                code = code + num;
            }
        }
        return code;
    }

    
}
