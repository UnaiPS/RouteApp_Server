/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.EmailException;
import exceptions.IncorrectPasswdException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.UserNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import messages.UserPasswd;
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


    @Override
    public void createUser(User user) throws CreateException {
        try{
            em.persist(user);
        }catch(Exception e){
                throw new CreateException(e.getMessage());
        }
    }
    
    @Override
    public User prueba(Long id, String fullName)  {
            return (User)em.createNamedQuery("prueba").setParameter("id", id).setParameter("fullName", fullName).getSingleResult();
    }

    @Override
    public void editUser(User user) throws EdittingException {
       
        try{
            em.merge(user);
            em.flush();
        }catch(Exception e){
            throw new EdittingException(e.getMessage());
        }
        
    }

    @Override
    public void removeUser(User user) throws DeleteException{
        try{em.remove(em.merge(user));
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
    public User findAccountByLogin(String login) throws UserNotFoundException {
        try {
            return (User)em.createNamedQuery("findAccountByLogin").setParameter("login", login).getSingleResult();
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }
    @Override
    public List<User> findAllDeliveryAccounts() {
        return em.createNamedQuery("findAllDeliveryAccounts").getResultList();
    }
    
    @Override
    public List<User> findAll() {
        return em.createNamedQuery("findAll").getResultList();
    }
    
    @Override
    public int forgottenpasswd(String email) throws EmailException{
        try {
            EmailSender.sendEmail(email);
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
        return 1;
    }
    
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
            //ERROR 
            throw new  IncorrectPasswdException();
        }
    }
}
