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
public class AddressExistException extends Exception {

    /**
     * Creates a new instance of <code>AddressExistException</code> without
     * detail message.
     */
    public AddressExistException() {
    }

    /**
     * Constructs an instance of <code>AddressExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AddressExistException(String msg) {
        super(msg);
    }
}
