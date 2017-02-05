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
public interface IBinaryTreeNode<T> {
    public IBinaryTreeNode insertLeftChild(IBinaryTreeNode node, T item);
    public IBinaryTreeNode insertRightChild(IBinaryTreeNode node, T item);
    public IBinaryTreeNode getLeftChild();
    public IBinaryTreeNode getRightChild();
    public T getItem();
    public void setItem(T item);
    public int compareTo(T item);
}
