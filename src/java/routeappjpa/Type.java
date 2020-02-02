package routeappjpa;

/**
 * The enum for the type of a coordinate.
 *
 * @author Jon Calvo Gaminde
 */
public enum Type {
    /**
     * A point inside the route.
     */
    WAYPOINT,
    /**
     * The staring point of the route.
     */
    ORIGIN,
    /**
     * A requiered point of the route.
     */
    DESTINATION,
    /**
     * The point of the worker.
     */
    GPS
}
