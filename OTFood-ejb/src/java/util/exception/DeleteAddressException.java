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
public class DeleteAddressException extends Exception {

    /**
     * Creates a new instance of <code>DeleteAddressException</code> without
     * detail message.
     */
    public DeleteAddressException() {
    }

    /**
     * Constructs an instance of <code>DeleteAddressException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteAddressException(String msg) {
        super(msg);
    }
}
