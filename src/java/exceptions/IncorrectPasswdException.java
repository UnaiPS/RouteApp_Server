package exceptions;

/**
 * An Exception that is throwed in some server methods.
 *
 * @author Daira Eguzkiza
 */
public class IncorrectPasswdException extends Exception {

    public IncorrectPasswdException(String msg) {
        super(msg);
    }
}
