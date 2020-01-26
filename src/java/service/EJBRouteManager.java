/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.EdittingException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import routeappjpa.Direction;
import routeappjpa.FullRoute;
import routeappjpa.Route;
import routeappjpa.User;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import routeappjpa.Coordinate_Route;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Stateless
public class EJBRouteManager implements RouteManagerLocal{

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    @EJB
    private CoordinateManagerLocal ejbCoordinate;

    @Override
    public void createRoute(FullRoute fullRoute) throws CreateException{
        try{
            Route route = fullRoute.getRoute();
            Set<Direction> directions = fullRoute.getDirections();
            em.persist(route);
            for(Coordinate_Route segment : fullRoute.getRoute().getCoordinates()) {
                segment.setRoute(findRoute(route.getId()));
                ejbCoordinate.createCoordinateRoute(segment);
            }
            for (Direction direction : directions) {
                ejbCoordinate.createCoordinate(direction.getCoordinate());
                ejbCoordinate.createDirection(direction);
            }
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
        
    }

    @Override
    public void updateRoute(Route route) throws EdittingException{
        try{
            //Logger.getLogger(EJBRouteManager.class.getName()).severe(fullRoute.getRoute().getCoordinates().toArray()[0].toString());
            em.merge(route);
            em.flush();
        }catch(Exception e){
            throw new EdittingException(e.getMessage());
        }
    }

    @Override
    public void removeRoute(Long routeId) throws DeleteException{
        try {
            Route route = findRoute(routeId);
            for(Coordinate_Route segment : route.getCoordinates()) {
                ejbCoordinate.removeCoordinateRoute(segment);
            }
            
            em.remove(route);
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
            return em.createNamedQuery("findByAssignedUser").setParameter("assignedTo", em.find(User.class, userId)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }
    
}
