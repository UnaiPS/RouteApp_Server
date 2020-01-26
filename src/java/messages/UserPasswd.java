package messages;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * This entity defines a user of the application with its respective data.
 * @author Daira Eguzkiza Lamelas
 */

public class UserPasswd implements Serializable{
        private Long id;
        private String login;
        private String fullName;
        private String email;
        private String oldpassword;
        private String newpassword;
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

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.login);
        hash = 89 * hash + Objects.hashCode(this.fullName);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.oldpassword);
        hash = 89 * hash + Objects.hashCode(this.newpassword);
        hash = 89 * hash + Objects.hashCode(this.lastPasswordChange);
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
        final UserPasswd other = (UserPasswd) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.oldpassword, other.oldpassword)) {
            return false;
        }
        if (!Objects.equals(this.newpassword, other.newpassword)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.lastPasswordChange, other.lastPasswordChange)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserPasswd{" + "id=" + id + ", login=" + login + ", fullName=" + fullName + ", email=" + email + ", oldpassword=" + oldpassword + ", newpassword=" + newpassword + ", lastPasswordChange=" + lastPasswordChange + '}';
    }

    


}
