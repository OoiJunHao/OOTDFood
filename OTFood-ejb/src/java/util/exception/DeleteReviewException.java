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
public class DeleteReviewException extends Exception {

    /**
     * Creates a new instance of <code>DeleteReviewException</code> without
     * detail message.
     */
    public DeleteReviewException() {
    }

    /**
     * Constructs an instance of <code>DeleteReviewException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteReviewException(String msg) {
        super(msg);
    }
}
