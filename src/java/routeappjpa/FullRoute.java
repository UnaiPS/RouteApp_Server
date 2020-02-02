package routeappjpa;

import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The entity for the route with directions.
 *
 * @author Jon Calvo Gaminde
 */
@XmlRootElement
public class FullRoute {

    private Route route;
    private Set<Direction> directions;

    //Constructors
    public FullRoute() {
    }

    public FullRoute(Route route, Set<Direction> directions) {
        this.route = route;
        this.directions = directions;
    }

    //Getters
    public Route getRoute() {
        return route;
    }

    public Set<Direction> getDirections() {
        return directions;
    }

    //Setters
    public void setRoute(Route route) {
        this.route = route;
    }

    public void setDirections(Set<Direction> directions) {
        this.directions = directions;
    }

}
