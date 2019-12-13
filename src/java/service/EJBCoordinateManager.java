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
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Type;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Stateless
public class EJBCoordinateManager implements CoordinateManagerLocal{

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;

    @Override
    public void createCoordinate(Coordinate coordinate) throws CreateException{
        try{
            em.persist(coordinate);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
        
    }

    @Override
    public void updateCoordinate(Coordinate coordinate) throws UpdateException{
        try{
            em.merge(coordinate);
            em.flush();
        }catch(Exception e){
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public void removeCoordinate(Long coordinateId) throws DeleteException{
        try {
            em.remove(em.merge(findCoordinate(coordinateId)));
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }

    @Override
    public Coordinate findCoordinate(Long id) throws FindException{
        try {
            return em.find(Coordinate.class, id);
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }
    
    @Override
    public List<Coordinate> findByType(Type type) throws FindException{
        try {
            return em.createNamedQuery("findCoordinatesByType").setParameter("type", type).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        
    }
    
    @Override
    public Long getIdByData(Coordinate coordinate) throws FindException{
        try {
            return (Long) em.createNamedQuery("getCoordinateIdByData")
                    .setParameter("latitude", coordinate.getLatitude())
                    .setParameter("longitude", coordinate.getLongitude())
                    .setParameter("type", coordinate.getType()).getSingleResult();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
    }

    @Override
    public void updateCoordinateRoute(Coordinate_Route visited) throws UpdateException{
        try{
            em.merge(visited);
            em.flush();
        }catch(Exception e){
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public void createDirection(Direction direction) throws CreateException {
        try{
            em.persist(direction);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
    }

    
    
}
