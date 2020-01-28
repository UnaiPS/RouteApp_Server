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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
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
    private Logger LOGGER = Logger.getLogger("EJBCoordinateManager");

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
    
    @Override
    public List<Direction> findDirectionsByRoute(String routeId) throws FindException{
        List<Direction> directions = null;
        try {
            directions = em.createNamedQuery("findDirectionsByRoute").setParameter("routeId",Long.parseLong(routeId)).getResultList();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }
        return directions;
    }
    
    @Override
    public void updateCoordinateRoute (Coordinate_Route visited, Double latitude, Double longitude) throws UpdateException{
        try{
            Coordinate gps = new Coordinate();
            gps.setLatitude(latitude);
            gps.setLongitude(longitude);
            gps.setType(Type.GPS);
            gps.setId(createCoordinate(gps));
            visited.setVisited(gps.getId());
            em.merge(visited);
            em.flush();
        }catch(Exception e){
            throw new UpdateException(e.getMessage());
        }
    }
    
    @Override
    public Long createCoordinate(Coordinate coordinate) throws CreateException{
        try{
            Long id = getIdByData(coordinate);
            if (id == null) {
                em.persist(coordinate);
                id = coordinate.getId();
            }
            return id;
        }catch(Exception e){
            throw new CreateException(e.getMessage()+"Id es: "+coordinate.getId());
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
    public void removeCoordinate(Coordinate coordinate) throws DeleteException{
        try {
            if (!coordinate.getType().equals(Type.WAYPOINT)) {
                try {
                    Direction direction = (Direction) em.createNamedQuery("findDirectionByCoordinate").setParameter("coordinate",coordinate).getSingleResult();
                    em.remove(em.merge(direction));

                } catch (NoResultException e) {
                    LOGGER.info("No direction exists for this coordinate: " + coordinate.toString());
                }
            }
            em.remove(em.merge(coordinate));
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
    public void createDirection(Direction direction) throws CreateException {
        try{
            direction.setCoordinate(findCoordinate(getIdByData(direction.getCoordinate())));
            em.createNamedQuery("findDirectionByCoordinate").setParameter("coordinate",direction.getCoordinate()).getSingleResult();
            LOGGER.info("Direction already exists.");
            
        }catch(NoResultException e){
            em.persist(direction);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
    }

    

    @Override
    public void createCoordinateRoute(Coordinate_Route segment) throws CreateException {
        try {
            segment.getCoordinate().setId(createCoordinate(segment.getCoordinate()));
            segment.setCoordinate(findCoordinate(segment.getCoordinate().getId()));
            em.persist(segment);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    @Override
    public void removeCoordinateRoute(Coordinate_Route segment) throws DeleteException {
        try {
            Coordinate coordinate = segment.getCoordinate();
            em.remove(em.merge(segment));
            

            try {
                em.createNamedQuery("findCoordinateRoutesByCoordinateId").setParameter("id",coordinate.getId()).getSingleResult();
            } catch (NoResultException e) {
                removeCoordinate(coordinate);
            }
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }
    }
    
}
