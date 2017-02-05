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
public class CBinaryTreeNode<T> implements IBinaryTreeNode<T> {
    protected T item;
    CBinaryTreeNode left, right;
    
    public CBinaryTreeNode() {
    }
    
    public CBinaryTreeNode(T item) {
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
    public CBinaryTreeNode getLeftChild() {
        return this.left;
    }

    @Override
    public CBinaryTreeNode getRightChild() {
        return this.right;
    }

    @Override
    public CBinaryTreeNode insertLeftChild(IBinaryTreeNode node, T item) {
        left = (CBinaryTreeNode)node;
        left.item = item;
        return left;
    }

    @Override
    public CBinaryTreeNode insertRightChild(IBinaryTreeNode node, T item) {
        right = (CBinaryTreeNode)node;
        right.item = item;
        return right;
    }

    @Override
    public int compareTo(T item) {
        return this.item.toString().compareTo((item.toString()));
    }
    
    public static String toPreOrderString(CBinaryTreeNode root) {
        String preOrder = "";
        if(root != null) {
            preOrder += root.toString() + " ";
            preOrder += toPreOrderString(root.left);
            preOrder += toPreOrderString(root.right);
        }
        return preOrder;
    }
    
    public static String toPostOrderString(CBinaryTreeNode root) {
        String postOrder = "";
        if(root != null) {
            postOrder += toPostOrderString(root.left);
            postOrder += toPostOrderString(root.right);
            postOrder += root.toString() + " ";
        }
        return postOrder;
    }
    
    public static String toInOrderString(CBinaryTreeNode root) {
        String inOrder = "";
        if(root != null) {
            inOrder += toInOrderString(root.left);
            inOrder += root.toString() + " ";
            inOrder += toInOrderString(root.right);
        }
        return inOrder;
    }
}
