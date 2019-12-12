/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Unai Pérez Sánchez
 */
public class FindException extends Exception {

    /**
     * Creates a new instance of <code>FindException</code> without detail
     * message.
     */
    public FindException() {
    }

    /**
     * Constructs an instance of <code>FindException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FindException(String msg) {
        super(msg);
    }
}
