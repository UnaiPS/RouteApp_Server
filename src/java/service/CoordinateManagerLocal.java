package service;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.FindException;
import exceptions.UpdateException;
import java.util.List;
import routeappjpa.Coordinate;
import routeappjpa.Coordinate_Route;
import routeappjpa.Direction;

/**
 * The interface for the EJB of the Coordinate entity.
 *
 * @author Jon Calvo Gaminde
 */
public interface CoordinateManagerLocal {

    /**
     * A method that inserts a coordinate in the Database.
     *
     * @param coordinate The coordinate to insert.
     * @return The new id of the coordinate.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    public Long createCoordinate(Coordinate coordinate) throws CreateException;

    /**
     * A method that updates a coordinate in the Database.
     *
     * @param coordinate The coordinate to update.
     * @throws UpdateException An exception thrown if there was an error
     * updating the entity.
     */
    public void updateCoordinate(Coordinate coordinate) throws UpdateException;

    /**
     * A method that removes a coordinate in the Database.
     *
     * @param coordinate The coordinate to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    public void removeCoordinate(Coordinate coordinate) throws DeleteException;

    /**
     * A method that finds a coordinate by id in the Database.
     *
     * @param id The id of the coordinate.
     * @return The found coordinate.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public Coordinate findCoordinate(Long id) throws FindException;

    /**
     * A method that finds the coordinate by type in the Database.
     *
     * @param type The type of the coordinates.
     * @return The coordinate found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<Coordinate> findByType(String type) throws FindException;

    /**
     * A method that creates a direction in the Database.
     *
     * @param direction The direction to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    public void createDirection(Direction direction) throws CreateException;

    /**
     * A method that finds the coordinate id by the rest of the data in the
     * Database.
     *
     * @param coordinate The data of the coordinate.
     * @return The coordinate id.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public Long getIdByData(Coordinate coordinate) throws FindException;

    /**
     * A method that inserts and sets a visited GPS coodinate to a
     * coordinate_route in the Database.
     *
     * @param gps The GPS coordinate.
     * @param routeId The routeId of the coordinate_route.
     * @param coordinateId The coordinateId of the coordinate_route.
     * @return The new id of the GPS coordinate.
     * @throws UpdateException An exception thrown if there was an error
     * updating the entity.
     */
    public Long updateCoordinateRoute(Coordinate gps, Long routeId, Long coordinateId) throws UpdateException;

    /**
     * A method that finds the directions by coordinate type in the Database.
     *
     * @param type The type of the coordinates.
     * @return The directions found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<Direction> findDirectionsByType(String type) throws FindException;

    /**
     * A method that finds the directions by a route in the Database.
     *
     * @param routeId The id of the route.
     * @return The directions found.
     * @throws FindException An exception thrown if there was an error finding
     * the entity.
     */
    public List<Direction> findDirectionsByRoute(String routeId) throws FindException;

    /**
     * A method that creates a coordinate_route in the Database.
     *
     * @param segment The coordinate_route to insert.
     * @throws CreateException An exception thrown if there was an error
     * creating the entity.
     */
    public void createCoordinateRoute(Coordinate_Route segment) throws CreateException;

    /**
     * A method that removes a coordinate_route in the Database.
     *
     * @param segment The coordinate_route to remove.
     * @throws DeleteException An exception thrown if there was an error
     * removing the entity.
     */
    public void removeCoordinateRoute(Coordinate_Route segment) throws DeleteException;
}
