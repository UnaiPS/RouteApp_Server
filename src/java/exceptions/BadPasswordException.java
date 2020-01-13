/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Daira Eguzkiza
 */
public class BadPasswordException extends Exception {
    public BadPasswordException(String msg){
        super(msg);
    }
}

