package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Daira Eguzkiza
 */
public class EmailException extends Exception {

    public EmailException(String msg) {
        super(msg);
    }
}
