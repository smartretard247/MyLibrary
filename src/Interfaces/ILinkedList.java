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
public interface ILinkedList<T> {
    public boolean isFull();
    public boolean isEmpty();
    public T insertHead(T item);
    public T insertTail(T item);
    public T removeHead() throws LinkedListException;
    public T removeTail() throws LinkedListException;
    public T removeElementAt(int i) throws LinkedListException;
    public T peekHead() throws LinkedListException;
    public T peekTail() throws LinkedListException;
    public T peekElementAt(int i) throws LinkedListException;
    public void empty();

    public static class LinkedListException extends Exception {
        String theMessage;
        
        public LinkedListException() {
            this("Unknown QueueException");
        }

        public LinkedListException(String message) {
            theMessage = message;
        }
        
        @Override
        public String getMessage() {
            return theMessage;
        }
    }
}
