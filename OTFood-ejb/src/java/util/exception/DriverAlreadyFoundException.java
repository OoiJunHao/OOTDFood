/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author benny
 */
public class DriverAlreadyFoundException extends Exception {

    /**
     * Creates a new instance of <code>DriverAlreadyFoundException</code>
     * without detail message.
     */
    public DriverAlreadyFoundException() {
    }

    /**
     * Constructs an instance of <code>DriverAlreadyFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DriverAlreadyFoundException(String msg) {
        super(msg);
    }
}
