package routeappjpa;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The entity for the route.
 *
 * @author Unai Pérez Sánchez
 */
@NamedQueries({
    @NamedQuery(
            name = "findAllRoutes",
            query = "SELECT r FROM Route r ORDER BY r.id")
    ,
    @NamedQuery(
            name = "findByAssignedUser",
            query = "SELECT r FROM Route r WHERE r.assignedTo=:assignedTo")
    ,
    @NamedQuery(
            name = "updateRouteByQuery",
            query = "UPDATE Route SET name = :name, mode = :mode, transportMode = :transportMode, trafficMode = :trafficMode WHERE id = :id")
})
@Entity
@Table(name = "route", schema = "routesdb")
@XmlRootElement
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * To identificate the route, the route will have an id of type Long
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * The coordinates of the route with the attributes of all the points of the
     * route
     */
    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    private Set<Coordinate_Route> coordinates;
    /**
     * The name of the route that the administrator assigned
     */
    @NotNull
    private String name;
    /**
     * The information about the administrator that created the route
     */
    @ManyToOne
    private User createdBy;
    /**
     * The delivery man/technician assigned to the route
     */
    @ManyToOne
    private User assignedTo;
    /**
     * The distance that the driver will be doing
     */
    private Double totalDistance;
    /**
     * How many time the system expected to make the route
     */
    private Integer estimatedTime;
    /**
     * If the route has been started or not
     */
    private Boolean started;
    /**
     * If the Route has been ended or not
     */
    private Boolean ended;
    /**
     * The mode of the route (FASTEST,SHORTEST,BALANCED)
     */
    private Mode mode;
    /**
     * The type of the vehicle that the driver is going to use
     */
    private TransportMode transportMode;
    /**
     * When is creating the route if takes into account the traffic at the
     * moment
     */
    private TrafficMode trafficMode;

    //Getters
    public Long getId() {
        return id;
    }

    public Set<Coordinate_Route> getCoordinates() {
        return coordinates;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public String getName() {
        return name;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public Boolean getStarted() {
        return started;
    }

    public Boolean getEnded() {
        return ended;
    }

    public Mode getMode() {
        return mode;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public TrafficMode getTrafficMode() {
        return trafficMode;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCoordinates(Set<Coordinate_Route> coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public void setTrafficMode(TrafficMode trafficMode) {
        this.trafficMode = trafficMode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.coordinates);
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.createdBy);
        hash = 79 * hash + Objects.hashCode(this.assignedTo);
        hash = 79 * hash + Objects.hashCode(this.totalDistance);
        hash = 79 * hash + Objects.hashCode(this.estimatedTime);
        hash = 79 * hash + Objects.hashCode(this.started);
        hash = 79 * hash + Objects.hashCode(this.ended);
        hash = 79 * hash + Objects.hashCode(this.mode);
        hash = 79 * hash + Objects.hashCode(this.transportMode);
        hash = 79 * hash + Objects.hashCode(this.trafficMode);
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
        final Route other = (Route) obj;
        if (!Objects.equals(this.coordinates, other.coordinates)) {
            return false;
        }
        if (!Objects.equals(this.totalDistance, other.totalDistance)) {
            return false;
        }
        if (this.mode != other.mode) {
            return false;
        }
        if (this.transportMode != other.transportMode) {
            return false;
        }
        if (this.trafficMode != other.trafficMode) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name=" + name + ", createdBy=" + createdBy + ", assignedTo=" + assignedTo + ", totalDistance=" + totalDistance + ", estimatedTime=" + estimatedTime + ", started=" + started + ", ended=" + ended + ", mode=" + mode + ", transportMode=" + transportMode + ", trafficMode=" + trafficMode + '}';
    }

}
