/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

/**
 *
 * @author Jeezy
 * @param <T>
 */
public interface IStack<T> {
    public boolean isFull();
    public boolean isEmpty();
    public void push(T item);
    public T peek() throws StackException;
    public T pop() throws StackException;
    public void empty();

    public static class StackException extends Exception {
        String theMessage;
        
        public StackException() {
            this("Unknown StackException");
        }

        public StackException(String message) {
            theMessage = message;
        }
        
        @Override
        public String getMessage() {
            return theMessage;
        }
    }
}
