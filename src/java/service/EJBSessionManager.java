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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;
import routeappjpa.Session;
import routeappjpa.Type;
import routeappjpa.User;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Stateless
public class EJBSessionManager implements SessionManagerLocal{

    @PersistenceContext(unitName = "RouteJPAPU")
    private EntityManager em;
    private final int MINUTES = 30;

    @Override
    public Session getSession(User user) throws CreateException, DeleteException{
        Session session = null;
        try{
            session = (Session) em.createNamedQuery("getSessionCode")
                .setParameter("logged", user).getSingleResult();
            if (session.getLastAction().getTime() + MINUTES*60000 < Instant.now().toEpochMilli()) {
                try{
                    em.remove(em.merge(session));
                } catch(Exception e) {
                    throw new DeleteException(e.getMessage());
                }
                throw new NoResultException("The user has been inactive for " + MINUTES + " minutes. Creating new Session.");
            }
        } catch (NoResultException e) {
            session = new Session();
            session.setLogged(user);
            session.setCode(createCode());
            session.setLastAction(Timestamp.from(Instant.now()));
        } catch(DeleteException e){
            throw e;
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
        return session;
    }

    @Override
    public User checkSession(String encryptSession) throws FindException {
        User user = null;
        try{
            //TODO Encryptation
            String code = encryptSession.substring(0, 6);
            Long millis = Long.getLong(encryptSession.substring(6));
            Session session = (Session) em.createNamedQuery("findSessionByCode")
                .setParameter("code", code).getSingleResult();
            long lastAction = session.getLastAction().getTime();
            if (lastAction > millis || millis > Timestamp.from(Instant.now()).getTime()) {
                user = session.getLogged();
            } else {
                throw new NoResultException();
            }
        } catch (NoResultException e) {
            throw new FindException("Invalid code.");
        }catch(Exception e){
            throw new FindException(e.getMessage());
        }
        return user;
    }
    
    private String createCode() {
        String code = "";
        for (int i = 0; i < 6; i++) {
            if (Math.random() < 0.5) {
                int num = (int) Math.floor(Math.random()*(91-65)+65);
                if (Math.random() < 0.5) {
                    num += 32;
                }
                char letter = (char) num;
                code = code + letter;
            } else {
                int num = (int) Math.floor(Math.random()*10);
                code = code + num;
            }
        }
        return code;
    }
    
}
