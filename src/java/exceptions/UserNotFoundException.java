package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Daira Eguzkiza
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
