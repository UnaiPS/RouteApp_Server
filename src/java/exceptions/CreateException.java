package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Unai Pérez Sánchez
 */
public class CreateException extends Exception {

    /**
     * Constructs an instance of CreateException with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public CreateException(String msg) {
        super(msg);
    }

}
