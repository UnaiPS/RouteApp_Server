/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import routeappjpa.User;

/**
 *
 * @author Daira Eguzkiza
 */
@Stateless
public class EJBUser<T> {

    private Class<T> entityClass;
    
    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;


    public void createUser(User user) throws CreateException {
        try{
            em.persist(user);
        }catch(Exception e){
                throw new CreateException(e.getMessage());
        }
    }

    public void editUser(User user) throws EdittingException {
       
        try{
            em.merge(user);
            em.flush();
        }catch(Exception e){
            throw new EdittingException(e.getMessage());
        }
        
    }

    public void removeUser(User user) throws DeleteException{
        try{em.remove(em.merge(user));
        }catch(Exception e){
            throw new DeleteException(e.getMessage());

        }
    }

    public User find(Long id) throws UserNotFoundException{
        try{
            return em.find(User.class, id);
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }

    public User findAccountByLogin(String login) throws UserNotFoundException {
        try {
            return em.find(User.class, login);
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
    }
    public List<User> findAllDeliveryAccounts() {
        return em.createNamedQuery("findAllDeliveryAccounts").getResultList();
    }
    
    public List<User> findAll() {
        return em.createNamedQuery("findAll").getResultList();
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
