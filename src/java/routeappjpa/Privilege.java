package routeappjpa;

/**
 * Type of privilege the user is going to have.
 * @author Daira Eguzkiza Lamelas
 */
public enum Privilege {
    /**
     * Determines that the user is a normal user without admin privileges.
     */
    USER,
    /**
     * Determines that the user is an administrator.
     */
    ADMIN
}
