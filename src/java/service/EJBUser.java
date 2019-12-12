/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.UserNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
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
    public User forgottenpasswd(String email){
        //ENVIAR EL EMAIL Y TAL
    }
    
    @Override
    public User editPasswd(UserPasswd user){
        User olduser = (User)em.createNamedQuery("findAccountByLogin").setParameter("login", user.getLogin()).getSingleResult();
        if(user.getOldpassword().equals(olduser.getPassword())){
            Date date = new Date(); 
            user.setLastPasswordChange(date);
        Object lala = em.createNamedQuery("editPasswd")
                .setParameter("login", user.getLogin()).setParameter("data", user).getSingleResult();
        }else{
            //ERROR
        }
    
    }
/*
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    */
}
