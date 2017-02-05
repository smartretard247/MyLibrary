/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.IBinaryTree;
import Interfaces.IBinaryTreeNode;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Jeezy
 * @param <T>
 */
public class BinaryTree<T> implements IBinaryTree<T> {
    protected String nameOfTree;
    private BinaryTreeNode theRoot = new BinaryTreeNode();
    
    public BinaryTree() {
        nameOfTree = "Default";
    }
    public BinaryTree(String name) {
        nameOfTree = name;
    }
    public BinaryTree(Scanner theScanner, boolean ignoreSpaces) {
        nameOfTree = "Default";
        read(theScanner, ignoreSpaces);
    }
    
    public final void read(Scanner theScanner, boolean ignoreSpaces) {
        if(!ignoreSpaces) {
            while(theScanner.hasNext()) {
                insertNode(getRoot(), new BinaryTreeNode(), (T)theScanner.next());
            }
        } else {
            while(theScanner.hasNext()) {
                insertNode(getRoot(), new BinaryTreeNode(), (T)theScanner.nextLine());
            }
        }
    }
    
    public void setName(String name) {
        System.out.println("Changing name of tree to: " + name);
        nameOfTree = name;
    }
    
    public String getName() {
        return nameOfTree;
    }
    
    @Override
    public final BinaryTreeNode setRoot(IBinaryTreeNode node) {
        BinaryTreeNode newRoot = (BinaryTreeNode)node;
        return theRoot = newRoot;
    }
    
    @Override
    public BinaryTreeNode getRoot() {
        return theRoot;
    }

    @Override
    public boolean isFull() {
        return false; //never full
    }

    @Override
    public boolean isEmpty() {
        return getRoot().item == null;
    }

    @Override
    public BinaryTreeNode findElement(T item) {
        try {
            return this.findElement(getRoot(), item);
        } catch (BinaryTreeException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    @Override
    public BinaryTreeNode findElement(IBinaryTreeNode root, T item) throws BinaryTreeException {
        BinaryTreeNode currentNode = (BinaryTreeNode)root;
        BinaryTreeNode toFind = new BinaryTreeNode(item);
        
        if(currentNode.item == null) throw new BinaryTreeException("Node Not Found");
        if(currentNode.item.equals(toFind.item)) return currentNode; //found match at root
        
        boolean leftSide = currentNode.compareTo(toFind) > 0; //should we look in left or right side of tree
        if(leftSide) {
            if(currentNode.left == null) throw new BinaryTreeException("Node Not Found"); //base case, left object is null
            if(currentNode.left.equals(toFind.item)) return currentNode.left; //found match on left side
            return findElement(currentNode.left, item); //recur down the left side of tree
        } else { //search ride side of tree
            if(currentNode.right == null) throw new BinaryTreeException("Node Not Found"); //base case, right object is null
            if(currentNode.right.equals(toFind.item)) return currentNode.right; //found match on right side
            return findElement(currentNode.right, item); //recur down the right side of tree
        }
    }

    @Override
    public BinaryTreeNode insertNode(IBinaryTreeNode node, T item) {
        node = (BinaryTreeNode)node;
        return insertNode(theRoot, node, item);
    }
    
    @Override
    public BinaryTreeNode insertNode(IBinaryTreeNode root, IBinaryTreeNode node, T item) {
        BinaryTreeNode currentNode = (BinaryTreeNode)root;
        BinaryTreeNode toInsert = (BinaryTreeNode)node;
        toInsert.item = item;
        
        if(getRoot().item == null) { //then this should be the root
            System.out.print("Inserting Node to " + nameOfTree + "...\n" + toInsert.toString() + " (as root)\n"); //log the insertion
            setRoot(toInsert); 
            return toInsert;
        }
        
        boolean leftSide = toInsert.compareTo(currentNode) < 0; //what side to check
        if(leftSide) {
            if(currentNode.left == null) { //check for empty left side node
                System.out.print("Inserting Node to " + getName() + "...\n" + toInsert.toString() + " (as left)\n"); //log the insertion
                return currentNode.insertLeftChild(toInsert, toInsert.item); //so insert one...
            } else return insertNode(currentNode.left, toInsert, (T)toInsert.item); //else recur down the left side
        } else {
            if(currentNode.right == null) {
                System.out.print("Inserting Node to " + getName() + "...\n" + toInsert.toString() + " (as right)\n"); //log the insertion
                return currentNode.insertRightChild(toInsert, toInsert.item);
            } else return insertNode(currentNode.right, toInsert, (T)toInsert.item);
        }
    }
    
    @Override
    public void empty() {
        System.out.println("Tree Emptied...");
        theRoot.item = null;
        theRoot.left = null;
        theRoot.right = null;
    }
    
    public static int countNodes(BinaryTreeNode root) {
        if(root.item == null) return 0; //empty tree
        int count = 1;
        if(root.left != null) count += countNodes(root.left);
        if(root.right != null) count += countNodes(root.right);
        return count;
    }
    
    public static String toPreOrderString(BinaryTreeNode root) {
        String preOrder = "";
        if(root != null) {
            preOrder += root.toString() + "\n";
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
            postOrder += root.toString() + "\n";
        }
        return postOrder;
    }
    
    public static String toInOrderString(BinaryTreeNode root) {
        String inOrder = "";
        if(root != null) {
            inOrder += toInOrderString(root.left);
            inOrder += root.toString() + "\n";
            inOrder += toInOrderString(root.right);
        }
        return inOrder;
    }
    
    @Override
    public String toString() {
        String toReturn = "Name of Tree: " + nameOfTree + "\n";
        
        //count the nodes
        toReturn += "Node count: ";
        toReturn += String.format("%3d", countNodes(theRoot));
        toReturn += "\n";
        
        //in order print the tree
        toReturn += "In Order:\n";
        toReturn += BinaryTree.toInOrderString(theRoot);
        toReturn += "\n";
        
        //pre order print the tree
        toReturn += "Pre Order:\n";
        toReturn += BinaryTree.toPreOrderString(theRoot);
        toReturn += "\n";
        
        //post order print the tree
        toReturn += "Post Order:\n";
        toReturn += BinaryTree.toPostOrderString(theRoot);
        
        return toReturn;
    }
    
    public static void main (String args []) {
        //create and run a test case
        BinaryTree<Integer> test = new BinaryTree(); //empty tree
        test.insertNode(new BinaryTreeNode(), 7); //insert node
        test.insertNode(new BinaryTreeNode(), 2); //insert node
        test.insertNode(new BinaryTreeNode(), 1); //insert node
        test.setName("Test 1"); //set the name for output
        System.out.println(test);
        
        //test inserting nodes
        test.insertNode(new BinaryTreeNode(), 6);
        test.insertNode(new BinaryTreeNode(), 3);
        test.insertNode(new BinaryTreeNode(), 8);
        test.insertNode(new BinaryTreeNode(), 5);
        test.insertNode(new BinaryTreeNode(), 9);
        test.insertNode(new BinaryTreeNode(), 4);
        System.out.println(test); //debug again
        
        //find an existing element, then non-existing element
        BinaryTreeNode foundNode;
        System.out.print("Finding an existing Node: ");
        foundNode = (BinaryTreeNode)test.findElement(2); //search for an existing node
        System.out.println(foundNode);
        System.out.print("Finding a non-existing Node: ");
        foundNode = (BinaryTreeNode)test.findElement(0); //0 should not exist in the tree
        
        //next test, create using string of characters
        BinaryTree<String> test2 = new BinaryTree(new Scanner("B I N A R Y T R E E"),false); //use the scanner to parse the string
        test2.setName("My Binary Tree");
        System.out.println(test2);
        System.out.print("Finding all elements: ");
        //all following element should return successfully
        System.out.print(test2.findElement("B").toString());
        System.out.print(test2.findElement("I").toString());
        System.out.print(test2.findElement("N").toString());
        System.out.print(test2.findElement("A").toString());
        System.out.print(test2.findElement("R").toString());
        System.out.print(test2.findElement("Y").toString());
        System.out.print(test2.findElement("T").toString());
        System.out.print(test2.findElement("R").toString());
        System.out.print(test2.findElement("E").toString());
        System.out.print(test2.findElement("E").toString());
        System.out.println();
        
        //next test, use input from a file
        try {
            BinaryTree<String> test3 = new BinaryTree(new Scanner(new java.io.File("BinaryTreeData.txt")),true);
            test3.setName("Tree From File");
            System.out.println(test3);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    } // end main
}
