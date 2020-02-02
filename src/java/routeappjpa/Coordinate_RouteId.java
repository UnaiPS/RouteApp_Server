package routeappjpa;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 * The entity of the ManyToMany id for the coordinate route.
 *
 * @author Jon Calvo Gaminde
 */
@Embeddable
public class Coordinate_RouteId implements Serializable {
    private Long routeId;
    private Long coordinateId;

    //Constructors
    public Coordinate_RouteId() {
    }

    //Getters
    public Long getRouteId() {
        return routeId;
    }

    public Long getCoordinateId() {
        return coordinateId;
    }

    //Setters
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setCoordinateId(Long coordinateId) {
        this.coordinateId = coordinateId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.getRouteId());
        hash = 29 * hash + Objects.hashCode(this.getCoordinateId());
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
        final Coordinate_RouteId other = (Coordinate_RouteId) obj;
        if (!Objects.equals(this.routeId, other.routeId)) {
            return false;
        }
        if (!Objects.equals(this.coordinateId, other.coordinateId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coordinate_RouteId{" + "routeId=" + getRouteId() + ", coordinateId=" + getCoordinateId() + '}';
    }

    
}
