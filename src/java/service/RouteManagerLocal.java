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
import javax.ejb.Local;
import routeappjpa.FullRoute;
import routeappjpa.Route;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Local
public interface RouteManagerLocal {
    public void createRoute(FullRoute fullRoute) throws CreateException;

    public void updateRoute(Route route) throws EdittingException;

    public void removeRoute(Long routeId) throws DeleteException;

    public Route findRoute(Long id) throws FindException;

    public List<Route> findAllRoutes() throws FindException;
    
    public List<Route> findByAssignedUser(Long userId) throws FindException;
}
