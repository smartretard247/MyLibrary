/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes;

import Interfaces.IBinaryTreeNode;


/**
 *
 * @author Jeezy
 * @param <T>
 */
public class BinaryTreeNode<T> implements IBinaryTreeNode<T> {
    protected T item;
    BinaryTreeNode left;
    BinaryTreeNode right;
    
    public BinaryTreeNode() {
    }
    
    public BinaryTreeNode(T item) {
        this.item = item;
    }
    
    @Override
    public String toString() {
        if(item == null) return "";
        return item.toString();
    }
    
    @Override
    public T getItem() {
        return this.item;
    }
    
    @Override
    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
        return this.left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
        return this.right;
    }

    @Override
    public BinaryTreeNode insertLeftChild(IBinaryTreeNode node, T item) {
        left = (BinaryTreeNode)node;
        left.item = item;
        return left;
    }

    @Override
    public BinaryTreeNode insertRightChild(IBinaryTreeNode node, T item) {
        right = (BinaryTreeNode)node;
        right.item = item;
        return right;
    }

    @Override
    public int compareTo(T item) {
        return this.item.toString().compareTo((item.toString()));
    }
    
    public static String toPreOrderString(BinaryTreeNode root) {
        String preOrder = "";
        if(root != null) {
            preOrder += root.toString() + " ";
            preOrder += toPreOrderString(root.left);
            preOrder += toPreOrderString(root.right);
        }
        return preOrder;
    }
    
    public static String toPostOrderString(BinaryTreeNode root) {
        String postOrder = "";
        if(root != null) {
            postOrder += toPostOrderString(root.left);
            postOrder += toPostOrderString(root.right);
            postOrder += root.toString() + " ";
        }
        return postOrder;
    }
    
    public static String toInOrderString(BinaryTreeNode root) {
        String inOrder = "";
        if(root != null) {
            inOrder += toInOrderString(root.left);
            inOrder += root.toString() + " ";
            inOrder += toInOrderString(root.right);
        }
        return inOrder;
    }
}
