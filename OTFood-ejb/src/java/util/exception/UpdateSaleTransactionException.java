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
public class UpdateSaleTransactionException extends Exception {

    /**
     * Creates a new instance of <code>UpdateSaleTransactionException</code>
     * without detail message.
     */
    public UpdateSaleTransactionException() {
    }

    /**
     * Constructs an instance of <code>UpdateSaleTransactionException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateSaleTransactionException(String msg) {
        super(msg);
    }
}
