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
public class NoAddressFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoAddressFoundException</code> without
     * detail message.
     */
    public NoAddressFoundException() {
    }

    /**
     * Constructs an instance of <code>NoAddressFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAddressFoundException(String msg) {
        super(msg);
    }
}
