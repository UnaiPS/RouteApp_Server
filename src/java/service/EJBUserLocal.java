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
import exceptions.IncorrectPasswdException;
import exceptions.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;
import messages.UserPasswd;
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
    public List<User> findByPrivilege(String privilege);
    public List<User> findAll();
    public void forgottenpasswd(String email, String login) throws EmailException, DoesntMatchException;
    //public User editPasswd(UserPasswd user) throws IncorrectPasswdException, EdittingException;
    //public User prueba(Long id, String fullName);
    public Session login(User user) throws BadPasswordException, UserNotFoundException;

    public String emailConfirmation(User user);
}
