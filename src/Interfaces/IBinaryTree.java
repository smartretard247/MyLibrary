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
public interface IBinaryTree<T> {
    public boolean isFull();
    public boolean isEmpty();
    public IBinaryTreeNode getRoot();
    public IBinaryTreeNode setRoot(IBinaryTreeNode node);
    public IBinaryTreeNode insertNode(IBinaryTreeNode node, T item);
    public IBinaryTreeNode insertNode(IBinaryTreeNode root, IBinaryTreeNode node, T item);
    public IBinaryTreeNode findElement(T item);
    public IBinaryTreeNode findElement(IBinaryTreeNode startNode, T item) throws BinaryTreeException;
    public void empty();

    public static class BinaryTreeException extends Exception {
        String theMessage;
        
        public BinaryTreeException() {
            this("Unknown BinaryTreeException");
        }

        public BinaryTreeException(String message) {
            theMessage = message;
        }
        
        @Override
        public String getMessage() {
            return theMessage;
        }
    }
}
