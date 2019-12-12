/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.ejb.Local;
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
    public User findAccountByLogin(String login);
    public List<User> findAll();
}
