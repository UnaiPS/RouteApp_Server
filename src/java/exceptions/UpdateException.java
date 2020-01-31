package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Unai Pérez Sánchez
 */
public class UpdateException extends Exception {

    /**
     * Creates a new instance of UpdateException without detail message.
     */
    public UpdateException() {
    }

    /**
     * Constructs an instance of UpdateException with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public UpdateException(String msg) {
        super(msg);
    }
}
