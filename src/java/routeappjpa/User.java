package routeappjpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity defines a user of the application with its respective data.
 * @author Daira Eguzkiza Lamelas
 */

@Entity
@Table(name="user", schema="routesdb")
@NamedQueries({
@NamedQuery(
        name="findAllDeliveryAccounts", 
        query = "SELECT u FROM User u WHERE u.privilege=routeappjpa.Privilege.USER"),
@NamedQuery(name="findAll", query="select u from User u ORDER BY u.id"),
@NamedQuery(name="findAccountByLogin", query="select u from User u where u.login=:login")/*,
// Input parameters can only be used in the WHERE clause or HAVING clause of a query 
// así que búscate la vida!!!
@NamedQuery(name="editPasswd", 
        query="update User u set u.fullName = :data.fullName, u.email = :data.email, "
                + "u.password = :data.newpassword, u.lastPasswordChange = :data.lastPasswordChange"
                + " where u.login =:data.login")*/
        , @NamedQuery(name="prueba", query="select u from User u where u.id=:id and u.fullName=:fullName")
})
@XmlRootElement
public class User implements Serializable{
        private static final long serialVersionUID=1L;

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String login;
        private String fullName;
        private String email;
        private Status status;
        private Privilege privilege;
        private String password;
        @Temporal(TemporalType.TIMESTAMP)
        private Date lastAccess;
        @Temporal(TemporalType.TIMESTAMP)
        private Date lastPasswordChange;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.login);
        hash = 47 * hash + Objects.hashCode(this.fullName);
        hash = 47 * hash + Objects.hashCode(this.email);
        hash = 47 * hash + Objects.hashCode(this.status);
        hash = 47 * hash + Objects.hashCode(this.privilege);
        hash = 47 * hash + Objects.hashCode(this.password);
        hash = 47 * hash + Objects.hashCode(this.lastAccess);
        hash = 47 * hash + Objects.hashCode(this.lastPasswordChange);
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
        final User other = (User) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.privilege != other.privilege) {
            return false;
        }
        if (!Objects.equals(this.lastAccess, other.lastAccess)) {
            return false;
        }
        if (!Objects.equals(this.lastPasswordChange, other.lastPasswordChange)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login=" + login + ", fullName=" + 
                fullName + ", email=" + email + ", status=" + status + 
                ", privilege=" + privilege + ", password=" + password + 
                ", lastAccess=" + lastAccess + ", lastPasswordChange=" 
                + lastPasswordChange + '}';
    }
}
