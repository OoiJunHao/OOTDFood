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
public class MealExistsException extends Exception {

    /**
     * Creates a new instance of <code>MealExistsException</code> without detail
     * message.
     */
    public MealExistsException() {
    }

    /**
     * Constructs an instance of <code>MealExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MealExistsException(String msg) {
        super(msg);
    }
}
