/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Ooi Jun Hao
 */
public class NoSaleTransactionException extends Exception {

    /**
     * Creates a new instance of <code>NoSaleTransactionException</code> without
     * detail message.
     */
    public NoSaleTransactionException() {
    }

    /**
     * Constructs an instance of <code>NoSaleTransactionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSaleTransactionException(String msg) {
        super(msg);
    }
}
