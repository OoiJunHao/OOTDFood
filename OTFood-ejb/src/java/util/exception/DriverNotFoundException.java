/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author yuntiangu
 */
public class DriverNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DriverNotFoundException</code> without
     * detail message.
     */
    public DriverNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DriverNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DriverNotFoundException(String msg) {
        super(msg);
    }
}
