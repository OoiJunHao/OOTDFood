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
public class FaqExistException extends Exception {

    /**
     * Creates a new instance of <code>FaqExistException</code> without detail
     * message.
     */
    public FaqExistException() {
    }

    /**
     * Constructs an instance of <code>FaqExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FaqExistException(String msg) {
        super(msg);
    }
}
