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
public class IngredientDeductException extends Exception {

    /**
     * Creates a new instance of <code>IngredientDeductException</code> without
     * detail message.
     */
    public IngredientDeductException() {
    }

    /**
     * Constructs an instance of <code>IngredientDeductException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IngredientDeductException(String msg) {
        super(msg);
    }
}
