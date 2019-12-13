/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exceptions.DeleteException;
import exceptions.EdittingException;
import exceptions.CreateException;
import exceptions.EmailException;
import exceptions.IncorrectPasswdException;
import exceptions.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;
import messages.UserPasswd;
import routeappjpa.User;

/**
 *
 * @author Daira Eguzkiza
 */
@Local
public interface EJBUserLocal {
    public void createUser(User user) throws CreateException;
    
    public void editUser(User user) throws EdittingException;
    public void removeUser(User user) throws DeleteException;
    public User find(Long id) throws UserNotFoundException;
    public User findAccountByLogin(String login) throws UserNotFoundException;
    public List<User> findAllDeliveryAccounts();
    public List<User> findAll();
    public int forgottenpasswd(String email) throws EmailException;
    public User editPasswd(UserPasswd user) throws IncorrectPasswdException, EdittingException;
    public User prueba(Long id, String fullName);
}
