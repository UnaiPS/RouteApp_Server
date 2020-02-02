package routeappjpa;

/**
 * The enum for the route transport mode.
 *
 * @author Unai Pérez Sánchez
 */
public enum TransportMode {
    /**
     * A car with less than 6 seats.
     */
    CAR,
    /**
     * A car with more than 5 seats.
     */
    CAR_HOV,
    /**
     * A walking individual.
     */
    PEDESTRIAN,
    /**
     * A heavy truck.
     */
    TRUCK,
    /**
     * A road bicycle.
     */
    BICYCLE;
}
