package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Unai Pérez Sánchez
 */
public class DeleteException extends Exception {

    /**
     * Constructs an instance of DeleteException with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public DeleteException(String msg) {
        super(msg);
    }

}
