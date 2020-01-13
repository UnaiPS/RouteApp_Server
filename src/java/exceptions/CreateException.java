/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Unai Pérez Sánchez
 */

public class CreateException extends Exception {


    /**
     * Constructs an instance of <code>CreateException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CreateException(String msg) {
        super(msg);
    }

}
