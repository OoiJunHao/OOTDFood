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
public class CardCreationException extends Exception {

    /**
     * Creates a new instance of <code>CardCreationException</code> without
     * detail message.
     */
    public CardCreationException() {
    }

    /**
     * Constructs an instance of <code>CardCreationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CardCreationException(String msg) {
        super(msg);
    }
}
