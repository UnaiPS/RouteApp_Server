/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Set<Coordinate> coordinates;
    @NotNull
    private Set<Destination> destinations;
    @NotNull
    private Set<Coordinate> visitedDestinations;
    @NotNull
    private User createdBy;
    private User assignedTo;
    @NotNull
    private Origin origin;
    @NotNull
    private String name;
    private Double totalDistance;
    private Integer estimatedTime;
    private Boolean started;
    private Boolean ended;
    private Mode mode;
    private TransportMode transportMode;
    private TrafficMode trafficMode;
}
