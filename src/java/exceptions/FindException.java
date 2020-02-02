package exceptions;

/**
 * An Exception that is throwed in some server methods.
 * @author Unai Pérez Sánchez
 */
public class FindException extends Exception {

    /**
     * Creates a new instance of FindException without detail message.
     */
    public FindException() {
    }

    /**
     * Constructs an instance of with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FindException(String msg) {
        super(msg);
    }
}
