/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name="sesion", schema="routedbjpa")
public class Sesion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private User logged;
    @NotNull
    private String code;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
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
}
