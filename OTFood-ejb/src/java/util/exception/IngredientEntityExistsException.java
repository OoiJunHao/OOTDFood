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
public class IngredientEntityExistsException extends Exception {

    /**
     * Creates a new instance of <code>IngredientEntityExistsException</code>
     * without detail message.
     */
    public IngredientEntityExistsException() {
    }

    /**
     * Constructs an instance of <code>IngredientEntityExistsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public IngredientEntityExistsException(String msg) {
        super(msg);
    }
}
