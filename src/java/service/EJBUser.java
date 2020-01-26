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
import exceptions.IncorrectPasswdException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.DoesntMatchException;
import exceptions.UserNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;
import messages.UserPasswd;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.Status;
import routeappjpa.Type;
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

    @Override
    public void createUser(User user) throws CreateException {
        try{
            user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
            em.persist(user);
        }catch(Exception e){
                throw new CreateException(e.getMessage());
        }
    }
    /*
    @Override
    public User prueba(Long id, String fullName)  {
            return (User)em.createNamedQuery("prueba").setParameter("id", id).setParameter("fullName", fullName).getSingleResult();
    }
    */
    @Override
    public void editUser(User user) throws EdittingException {
        try{
            if (find(user.getId()).getLastPasswordChange().compareTo(user.getLastPasswordChange())<0) {
                user.setPassword(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())));
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
        Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, "Algo llega.");
        Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, user.getLogin());
        try{
            u = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
            if(Hasher.encrypt(Decrypt.descifrarTexto(user.getPassword())).equals(u.getPassword())){
                u.setLastAccess(new Date());
                return ejbSession.getSession(u);
            }else{
                throw new BadPasswordException("The data you've entered isn't correct.");
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
    public List<User> findByPrivilege(String privilege) {
        return em.createNamedQuery("findByPrivilege").setParameter("privilege",Privilege.valueOf(privilege)).getResultList();
    }
    
    @Override
    public List<User> findAll() {
        return em.createNamedQuery("findAll").getResultList();
    }
    
    
    @Override
    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException{
        try {
            User user = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
            if(user.getEmail().equals(email)){
                EmailSender es = new EmailSender("smtp.gmail.com", "587");
                //Aquí vendría todo lo de generar la nueva contraseña y meterla en la base de datos.
                String nuevaContra = createCode(10);
                
                user.setPassword(Hasher.encrypt(nuevaContra));
                editUser(user);
                es.sendEmail(email, nuevaContra, 0);
            }else{
                throw new DoesntMatchException("The login doesn't match with the email.");
            }
           
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
    }
    
    /*
    @Override
    public User editPasswd(UserPasswd user) throws IncorrectPasswdException, EdittingException{
        User olduser = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
        if(user.getOldpassword().equals(olduser.getPassword())){
            Date date = new Date(); 
            olduser.setLastPasswordChange(date);
            olduser.setPassword(user.getNewpassword());
            try{
               editUser(olduser);
               olduser=(User)em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
               olduser.setPassword(null);
               return olduser;
            }catch(Exception e){
                throw new EdittingException(e.getMessage());
            }
            }else{
            throw new IncorrectPasswdException("The password is not correct.");
        }
    }
    */
    
    @Override
    public String emailConfirmation(User user) {
        //TODO
        String code = createCode(4);
        try{
            EmailSender es = new EmailSender("smtp.gmail.com", "587");           
            es.sendEmail(user.getEmail(), code, 1);
        } catch (Exception ex) {
            Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
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
