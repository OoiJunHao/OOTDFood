/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Mitsuki
 */
public class UpdateDriverException extends Exception {

    /**
     * Creates a new instance of <code>UpdateDriverException</code> without
     * detail message.
     */
    public UpdateDriverException() {
    }

    /**
     * Constructs an instance of <code>UpdateDriverException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDriverException(String msg) {
        super(msg);
    }
}
