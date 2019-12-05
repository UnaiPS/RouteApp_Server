/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Unai Pérez Sánchez
 */
@Entity
@Table(name = "route",schema = "routedbjpa")
public class Route implements Serializable{
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
    @OneToMany
    @NotNull
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
    @NotNull
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
     * When is creating the route if takes into account the traffic at the moment
     */
    private TrafficMode trafficMode;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the coordinates
     */
    public Set<Coordinate_Route> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(Set<Coordinate_Route> coordinates) {
        this.coordinates = coordinates;
    }
    /**
     * @return the createdBy
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the assignedTo
     */
    public User getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the totalDistance
     */
    public Double getTotalDistance() {
        return totalDistance;
    }

    /**
     * @param totalDistance the totalDistance to set
     */
    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * @return the estimatedTime
     */
    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * @param estimatedTime the estimatedTime to set
     */
    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    /**
     * @return the started
     */
    public Boolean getStarted() {
        return started;
    }

    /**
     * @param started the started to set
     */
    public void setStarted(Boolean started) {
        this.started = started;
    }

    /**
     * @return the ended
     */
    public Boolean getEnded() {
        return ended;
    }

    /**
     * @param ended the ended to set
     */
    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return the transportMode
     */
    public TransportMode getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode the transportMode to set
     */
    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return the trafficMode
     */
    public TrafficMode getTrafficMode() {
        return trafficMode;
    }

    /**
     * @param trafficMode the trafficMode to set
     */
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
