package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author Dariel J. Carrión Rivera
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}			
		
		public String toString() { return value.toString(); }
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	@Override
	public void add(E e) {
		Node<E> newNode = new Node<>(e);
		if (this.head == null) 
			head = newNode;
		else if (currentSize == 1) {
			if (head.getValue().compareTo(e) < 1)
				this.head.setNext(newNode);
			else {
				Node<E> savedHead = head;
				head = newNode;
				newNode.setNext(savedHead);
			}
		}
		else {
			Node<E> currNode = head;
			if (currNode.getValue().compareTo(newNode.getValue()) > 1) {
//				System.out.println(currNode.toString() +" > "+ newNode.toString());
				if (currNode == head) {
					Node<E> savedHead = this.head;
					this.head = newNode;
					newNode.setNext(savedHead);
				}
			}
			else {
				while (currNode.getNext() != null) {
					if (currNode.getNext().getValue().compareTo(newNode.getValue()) < 1) {
//						System.out.println(currNode.getNext().toString() +" < "+ newNode.toString());
						currNode = currNode.getNext();
					}
					else break; //currNode.value == newNode.value
				}
				if (currNode.getValue().compareTo(newNode.getValue()) <= 1) {
//					System.out.println(currNode.toString() +" <= "+ newNode.toString());
					if (currNode == this.head) {
						this.head = newNode;
						newNode.setNext(currNode);
					}
					else {
						Node<E> savedNext = currNode.getNext();
						currNode.setNext(newNode);
						newNode.setNext(savedNext);
					}
				}
			}
		}
		currentSize++;
	}

	@Override
	public boolean remove(E e) {
		if (this.head == null) return false;
		if (this.head.getValue().equals(e)) {
			if (this.head.getNext() != null)
				this.head = this.head.getNext();
			if (currentSize == 1)
				this.head = null;
			currentSize--;
			return true;
		}
		
		Node<E> currNode = this.head;
		while (currNode.getNext() != null) {
			Node<E> next = currNode.getNext();
			if (next.getValue().equals(e)) {
				currNode.setNext(next.getNext());
				next.clear();
				currentSize--;
				return true;
			}
			currNode = next;
		}
		
		return false;
	}

	@Override
	public E removeIndex(int index) {
		if (index < 0 || index >= currentSize)
			throw new IndexOutOfBoundsException("Index is out of bounds");
		Node<E> rmNode, currNode = this.head;
		E value = null;
		
		while (index > 1) {
			currNode = currNode.getNext();
			index--;
		}
		
		if (index == 0) {
			rmNode = this.head;
			this.head = rmNode.getNext();
		}
		else {
			rmNode = currNode.getNext();
			currNode.setNext(rmNode.getNext());
		}
					
		value = rmNode.getValue();
		rmNode.clear();
		currentSize--;
		return value;
	}

	@Override
	public int firstIndex(E e) {
		int index = 0;
		
		Node<E> currNode = this.head;
		while (currNode != null) {
			if (currNode.getValue().equals(e))
				return index;
			index++;
			currNode = currNode.getNext();
		}

		return -1;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= currentSize)
			throw new IndexOutOfBoundsException("Index is out of bounds");
		if (index == 0) return this.head.getValue();
		Node<E> currNode = this.head;
		while (index > 0) {
			currNode = currNode.getNext();
			index--;
		}
		
		return currNode.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		if (this.currentSize == 0) return "";
		String s = "";
		Node<E> curr = this.head;
		while (curr != null) {
			s += curr.getValue().toString() + ", ";
			curr = curr.getNext();
		}
		return s.substring(0, s.length()-2);
	}
}
