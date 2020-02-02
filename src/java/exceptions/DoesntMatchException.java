package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Daira Eguzkiza
 */
public class DoesntMatchException extends Exception {

    public DoesntMatchException(String msg) {
        super(msg);
    }
}
