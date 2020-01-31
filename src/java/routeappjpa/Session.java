package routeappjpa;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The entity for the Session.
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name = "session", schema = "routesdb")
@NamedQueries({
    @NamedQuery(name = "getSessionCode",
            query = "SELECT s FROM Session s WHERE s.logged = :logged"
    )
    ,
    @NamedQuery(name = "findSessionByCode",
            query = "SELECT s FROM Session s WHERE s.code = :code"
    )
})
@XmlRootElement
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private User logged;
    @NotNull
    private String code;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAction;

    //Getters
    public String getCode() {
        return code;
    }

    public User getLogged() {
        return logged;
    }

    public Date getLastAction() {
        return lastAction;
    }

    //Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setLogged(User logged) {
        this.logged = logged;
    }

    public void setLastAction(Date lastAction) {
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
