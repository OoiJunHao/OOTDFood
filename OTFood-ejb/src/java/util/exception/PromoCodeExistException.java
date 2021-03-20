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
public class PromoCodeExistException extends Exception {

    /**
     * Creates a new instance of <code>PromoCodeExistException</code> without
     * detail message.
     */
    public PromoCodeExistException() {
    }

    /**
     * Constructs an instance of <code>PromoCodeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PromoCodeExistException(String msg) {
        super(msg);
    }
}
