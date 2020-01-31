package routeappjpa;

/**
 * The enum for the Route mode.
 * @author Unai Pérez Sánchez
 */
public enum Mode {
    /**
     * The fastest way of doing the route
     */
    FASTEST,
    /**
     * The shortest way of doing the route
     */
    SHORTEST,
    /**
     * A mix of the fastest and shortest route
     */
    BALANCED;
}
