/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
            if (getIdByData(coordinate) == null) {
				em.persist(coordinate);
			}
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
    public List<Coordinate> findByType(String type) throws FindException{
        List<Coordinate> coords=null;
        try {
            coords = em.createNamedQuery("findCoordinatesByType").setParameter("type",Type.valueOf(type)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return coords;
    }
    
    @Override
    public Long getIdByData(Coordinate coordinate) throws FindException{
        try {
            return (Long) em.createNamedQuery("getCoordinateIdByData")
                    .setParameter("latitude", coordinate.getLatitude())
                    .setParameter("longitude", coordinate.getLongitude())
                    .setParameter("type", coordinate.getType()).getSingleResult();
        } catch (NoResultException e) {
            return null;
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
            createCoordinate(direction.getCoordinate());
            em.persist(direction);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
    }

    @Override
    public List<Direction> findDirectionsByType(String type) throws FindException{
        List<Direction> directions = null;
        try {
            directions = em.createNamedQuery("findDirectionsByType").setParameter("type",Type.valueOf(type)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return directions;
    }
    
}
