/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name="session", schema="routesdb")
@XmlRootElement
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private User logged;
    @NotNull
    private String code;
    @NotNull
    private Timestamp lastAction;

    

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    

    /**
     * @return the logged
     */
    public User getLogged() {
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(User logged) {
        this.logged = logged;
    }

    /**
     * @return the lastAction
     */
    public Timestamp getLastAction() {
        return lastAction;
    }

    /**
     * @param lastAction the lastAction to set
     */
    public void setLastAction(Timestamp lastAction) {
        this.lastAction = lastAction;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.logged);
        hash = 23 * hash + Objects.hashCode(this.code);
        hash = 23 * hash + Objects.hashCode(this.lastAction);
        return hash;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Session other = (Session) obj;
        if (!Objects.equals(this.logged, other.logged)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Session{" + "logged=" + logged + ", lastAction=" + lastAction + '}';
    }
    
}
