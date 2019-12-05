/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author 2dam
 */
@Entity
@Table(name="coordinate_route", schema="routedbjpa")
public class Coordinate_Route implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Route route;
    @Id
    private Coordinate coordinate;
    private int order;
    private long visited;

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the visited
     */
    public long getVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(long visited) {
        this.visited = visited;
    }
}
