/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    private Date lastAction;

    

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
     * @return the lastAction
     */
    public Date getLastAction() {
        return lastAction;
    }

    /**
     * @param lastAction the lastAction to set
     */
    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
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
}
