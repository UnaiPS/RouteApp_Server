/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import routeappjpa.Privilege;
import routeappjpa.Session;
import routeappjpa.User;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface SessionManagerLocal {
    public Session getSession(User user) throws CreateException, DeleteException;
    
    public User checkSession(String encryptSession, Privilege requieredPrivilege) throws InternalServerErrorException,NotAuthorizedException, ForbiddenException;
}