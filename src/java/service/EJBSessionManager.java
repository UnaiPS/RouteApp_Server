/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import encryption.Decrypt;
import exceptions.CreateException;
import exceptions.DeleteException;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ForbiddenException;
import routeappjpa.Privilege;
import routeappjpa.Session;
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
    private Logger LOGGER = Logger.getLogger("EJBSessionManager");

    @Override
    public Session getSession(User user) throws CreateException, DeleteException{
        LOGGER.info("Preparing the session");
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
                throw new NoResultException("The user has been inactive for " + MINUTES + " minutes");
            } else {
                session.setLastAction(Date.from(Instant.now()));
                em.merge(session);
                em.flush();
            }
        } catch (NoResultException e) {
            LOGGER.info(e.getLocalizedMessage() + ". Creating a new Session.");
            session = new Session();
            session.setLogged(user);
            session.setCode(createCode());
            session.setLastAction(Date.from(Instant.now()));
            em.persist(session);
        } catch(DeleteException e){
            throw e;
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
        LOGGER.info("Session prepared");
        return session;
    }

    @Override
    public User checkSession(String encryptSession, Privilege requiredPrivilege) throws InternalServerErrorException,NotAuthorizedException, ForbiddenException {
        LOGGER.info("Checking session");
        User user = null;
        String code = null;
        Long millis = null;
        try{
            try{
                encryptSession = Decrypt.descifrarTexto(encryptSession);
                code = encryptSession.substring(0, 6);
                millis = Long.parseLong(encryptSession.substring(6));

            }catch (Exception ex){
                throw new BadRequestException("Malformed session code.");
            }
            Session session = (Session) em.createNamedQuery("findSessionByCode")
                .setParameter("code", code).getSingleResult();
            long lastAction = session.getLastAction().getTime()-3000L;
            if (lastAction > millis || millis > Instant.now().toEpochMilli()) {
                throw new NoResultException("Invalid code.");
            } else if (lastAction + MINUTES*60000 < Instant.now().toEpochMilli()) {
                throw new NoResultException("User has been inactive for too long.");
            } else if (requiredPrivilege != null && !session.getLogged().getPrivilege().equals(requiredPrivilege)) {
                throw new ForbiddenException("Wrong user privilege.");
            } else {
                user = session.getLogged();
                session.setLastAction(Date.from(Instant.now()));
                em.merge(session);
                em.flush();
            }
        } catch (NoResultException e) {
            LOGGER.severe("HTTP request denied. Reason: " + e.getMessage());
            throw new NotAuthorizedException(e.getMessage());
        } catch (ForbiddenException | BadRequestException e) {
            LOGGER.severe("HTTP request denied. Reason: " + e.getMessage());
            throw e;
        }catch(Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
        LOGGER.info("The session is valid");
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
