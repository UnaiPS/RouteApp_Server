package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Daira Eguzkiza
 */
public class BadPasswordException extends Exception {

    public BadPasswordException(String msg) {
        super(msg);
    }
}
