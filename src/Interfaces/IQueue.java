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
public interface IQueue<T> {
    public boolean isFull();
    public boolean isEmpty();
    public void put(T item);
    public T peek() throws QueueException;
    public T get() throws QueueException;
    public void empty();

    public static class QueueException extends Exception {
        String theMessage;
        
        public QueueException() {
            this("Unknown QueueException");
        }

        public QueueException(String message) {
            theMessage = message;
        }
        
        @Override
        public String getMessage() {
            return theMessage;
        }
    }
}
