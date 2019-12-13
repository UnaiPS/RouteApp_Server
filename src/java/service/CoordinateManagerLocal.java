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
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Type;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface CoordinateManagerLocal {
    public void createCoordinate(Coordinate coordinate) throws CreateException;

    public void updateCoordinate(Coordinate coordinate) throws UpdateException;

    public void removeCoordinate(Long coordinateId) throws DeleteException;

    public Coordinate findCoordinate(Long id) throws FindException;
    
    public List<Coordinate> findByType(Type type) throws FindException;
    
    public void createDirection(Direction direction) throws CreateException;

    public Long getIdByData(Coordinate coordinate) throws FindException;

    public void updateCoordinateRoute(Coordinate_Route visited) throws UpdateException;
}