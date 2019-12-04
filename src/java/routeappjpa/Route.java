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
     * The coordinates of the shape of the route (middle points between origin
     * and the destination)
     */
    @NotNull
    private Set<Coordinate> coordinates;
    /**
     * The route will have one or more destinations
     */
    @NotNull
    private Set<Destination> destinations;
    /**
     * The delivery man/technician when marks a destination as visited will be
     * added
     */
    @NotNull
    private Set<Coordinate> visitedDestinations;
    /**
     * The information about the administrator that created the route
     */
    @NotNull
    private User createdBy;
    /**
     * The delivery man/technician assigned to the route
     */
    private User assignedTo;
    /**
     * The start point of the route
     */
    @NotNull
    private Origin origin;
    /**
     * The name of the route that the administrator assigned
     */
    @NotNull
    private String name;
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
    public Set<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(Set<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return the destinations
     */
    public Set<Destination> getDestinations() {
        return destinations;
    }

    /**
     * @param destinations the destinations to set
     */
    public void setDestinations(Set<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * @return the visitedDestinations
     */
    public Set<Coordinate> getVisitedDestinations() {
        return visitedDestinations;
    }

    /**
     * @param visitedDestinations the visitedDestinations to set
     */
    public void setVisitedDestinations(Set<Coordinate> visitedDestinations) {
        this.visitedDestinations = visitedDestinations;
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
     * @return the origin
     */
    public Origin getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Origin origin) {
        this.origin = origin;
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
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.destinations, other.destinations)) {
            return false;
        }
        if (!Objects.equals(this.origin, other.origin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", destinations=" + destinations +
                ", createdBy=" + createdBy + ", assignedTo=" + assignedTo +
                ", origin=" + origin + ", name=" + name + ", totalDistance=" +
                totalDistance + ", estimatedTime=" + estimatedTime + ", started=" +
                started + ", ended=" + ended + ", mode=" + mode + ", transportMode=" +
                transportMode + ", trafficMode=" + trafficMode + '}';
    }
    
}
