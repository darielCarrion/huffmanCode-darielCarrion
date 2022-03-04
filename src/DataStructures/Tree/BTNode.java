package Tree;

import utils.BinaryTreePrinter.PrintableNode;

/**
 * This class represents a binary tree node with key, value,
 * left child, right child, and parent.
 * @author Juan O. Lopez & Fernando Bermudez
 *
 * @param <K> Generic type for the key of the values to be stored in the nodes
 * @param <V> Generic type for the values to be stored in the nodes
 */
public class BTNode<K extends Comparable<? super K>, V extends Comparable<? super V>> implements Comparable<BTNode<K,V>>, PrintableNode {
	private K key;
	private V value;
	private BTNode<K, V> leftChild, rightChild, parent;

	public BTNode() {
		this(null, null, null);
	}
	public BTNode(K key, V value) {
		this(key, value, null);
	}

	public BTNode(K key, V value, BTNode<K, V> parent) {
		this.key = key;
		this.value = value;
		this.parent = parent;
		this.leftChild = this.rightChild = null;
	}


	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public BTNode<K, V> getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(BTNode<K, V> leftChild) {
		this.leftChild = leftChild;
	}
	public BTNode<K, V> getRightChild() {
		return rightChild;
	}
	public void setRightChild(BTNode<K, V> rightChild) {
		this.rightChild = rightChild;
	}
	public BTNode<K, V> getParent() {
		return parent;
	}
	public void setParent(BTNode<K, V> newParent) {
		parent = newParent;
	}
	public void clear() {
		key = null;
		value = null;
		leftChild = rightChild = parent = null;
	}
	
	/**
	 *  This is the method talked about in the huffman_tree() method, 
	 *  that is delegated the task of determining which node is the left and right child
	 *  if the case occurred that two nodes have the same frequency
	 *  
	 *  @param n node that the target object will compare based on key or value 
	 *  @return a negative number, 0, or a positive number. Depends whether the target object is less than, equal or greater than n respectively.
	 */
	@Override
	public int compareTo(BTNode<K,V> n) {
		/*We first compare the FREQUENCIES, if they're not the same just return whatever that comparison determined*/
		final int freqComp = this.getKey().compareTo(n.getKey());
		if(freqComp != 0) return freqComp;
		
		/*If they have the same frequency then just return whatever the comparison between SYMBOLS determines*/
		return this.getValue().compareTo(n.getValue());
	}

	/* The methods below are merely to comply with the PrintableNode interface,
	 * used by the BinaryTreePrinter class to nicely display a binary tree.
	 * Could have renamed the methods, but just didn't feel like it.
	 */
	
	@Override
	public PrintableNode getLeft() {
		return getLeftChild();
	}
	@Override
	public PrintableNode getRight() {
		return getRightChild();
	}
	@Override
	public String getText() {
		return key.toString() + ":" + value.toString();
	}
	

}
