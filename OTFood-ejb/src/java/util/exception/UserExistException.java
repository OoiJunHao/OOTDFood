/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Ong Bik Jeun
 */
public class UserExistException extends Exception {

    /**
     * Creates a new instance of <code>UserExistException</code> without detail
     * message.
     */
    public UserExistException() {
    }

    /**
     * Constructs an instance of <code>UserExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserExistException(String msg) {
        super(msg);
    }
}
