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
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface CoordinateManagerLocal {
    public Long createCoordinate(Coordinate coordinate) throws CreateException;

    public void updateCoordinate(Coordinate coordinate) throws UpdateException;

    public void removeCoordinate(Coordinate coordinate) throws DeleteException;

    public Coordinate findCoordinate(Long id) throws FindException;
    
    public List<Coordinate> findByType(String type) throws FindException;
    
    public void createDirection(Direction direction) throws CreateException;

    public Long getIdByData(Coordinate coordinate) throws FindException;

    public void updateCoordinateRoute (Coordinate_Route visited, Double latitude, Double longitude) throws UpdateException;
    
    public List<Direction> findDirectionsByType(String type) throws FindException;
    
    public List<Direction> findDirectionsByRoute(String routeId) throws FindException;

    public void createCoordinateRoute(Coordinate_Route segment) throws CreateException;

    public void removeCoordinateRoute(Coordinate_Route segment) throws DeleteException;
}