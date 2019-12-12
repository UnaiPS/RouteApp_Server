/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exception.CreateException;
import exception.DeleteException;
import exception.FindException;
import exception.UpdateException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import routeappjpa.Route;
import routeappjpa.User;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Stateless
public class EJBRouteManager implements RouteManagerLocal{

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;

    @Override
    public void createRoute(Route route) throws CreateException{
        try{
            em.persist(route);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
        
    }

    @Override
    public void updateRoute(Route route) throws UpdateException{
        try{
            em.merge(route);
            em.flush();
        }catch(Exception e){
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public void removeRoute(Long routeId) throws DeleteException{
        try {
            em.remove(em.merge(findRoute(routeId)));
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }

    @Override
    public Route findRoute(Long id) throws FindException{
        try {
            return em.find(Route.class, id);
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }

    @Override
    public List<Route> findAllRoutes() throws FindException{
        try {
            return em.createNamedQuery("findAllRoutes").getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }
    
    @Override
    public List<Route> findByAssignedUser(Long userId) throws FindException{
        try {
            return em.createNamedQuery("findByAssignedUser").setParameter("user", em.find(User.class, userId)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }
    
}
