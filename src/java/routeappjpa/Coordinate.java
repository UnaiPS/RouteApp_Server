package routeappjpa;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The coordinate entity.
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name = "coordinate", schema = "routesdb")
@NamedQueries({
    @NamedQuery(name = "findCoordinatesByType",
            query = "SELECT c FROM Coordinate c WHERE c.type = :type"
    )
    ,
    @NamedQuery(name = "getCoordinateIdByData",
            query = "SELECT c.id FROM Coordinate c WHERE c.latitude = :latitude AND c.longitude = :longitude AND c.type = :type"
    )
    ,
    @NamedQuery(name = "deleteCoordinateById",
            query = "DELETE FROM Coordinate c WHERE c.id = :id"
    )
})
@XmlRootElement
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Type type;

    //Getters
    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Type getType() {
        return type;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.latitude);
        hash = 37 * hash + Objects.hashCode(this.longitude);
        hash = 37 * hash + Objects.hashCode(this.type);
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
        final Coordinate other = (Coordinate) obj;
        if (!Objects.equals(this.latitude, other.latitude)) {
            return false;
        }
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coordinate(" + latitude + "|" + longitude + ") " + type;
    }
}
