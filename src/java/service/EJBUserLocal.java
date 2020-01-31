/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.BadPasswordException;
import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.DoesntMatchException;
import exceptions.EmailException;
import exceptions.FindException;
import exceptions.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import routeappjpa.Session;
import routeappjpa.User;

/**
 *
 * @author Daira Eguzkiza
 */
@Local
public interface EJBUserLocal {

    public void createUser(User user) throws CreateException;

    public void editUser(User user) throws EdittingException;

    public void removeUser(Long id) throws DeleteException;

    public User find(Long id) throws UserNotFoundException;

    public User findAccountByLogin(String login) throws UserNotFoundException;

    public List<User> findByPrivilege(String privilege) throws FindException;

    public List<User> findAll() throws FindException;

    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException, ForbiddenException, UserNotFoundException;

    public Session login(User user) throws BadPasswordException, UserNotFoundException;

    public String emailConfirmation(User user) throws EmailException;
}
