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
public class UpdateReviewException extends Exception {

    /**
     * Creates a new instance of <code>UpdateReviewException</code> without
     * detail message.
     */
    public UpdateReviewException() {
    }

    /**
     * Constructs an instance of <code>UpdateReviewException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateReviewException(String msg) {
        super(msg);
    }
}
