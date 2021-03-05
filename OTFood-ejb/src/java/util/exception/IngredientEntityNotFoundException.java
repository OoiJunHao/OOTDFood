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
public class IngredientEntityNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>IngredientEntityNotFoundException</code>
     * without detail message.
     */
    public IngredientEntityNotFoundException() {
    }

    /**
     * Constructs an instance of <code>IngredientEntityNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public IngredientEntityNotFoundException(String msg) {
        super(msg);
    }
}
