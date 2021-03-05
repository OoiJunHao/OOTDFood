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
public class MealNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MealNotFoundException</code> without
     * detail message.
     */
    public MealNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MealNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MealNotFoundException(String msg) {
        super(msg);
    }
}
