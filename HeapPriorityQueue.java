
/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.Comparator;

/**
 * An implementation of a priority queue using an array-based heap.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser.
 */
public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> implements Comparator<Entry<K, V>> {
	/** primary collection of priority queue entries */
	protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

	/**
	 * Creates an empty priority queue based on the natural ordering of its keys.
	 */
	public HeapPriorityQueue() {
		super();
	}

	/**
	 * Creates an empty priority queue using the given comparator to order keys.
	 * 
	 * @param comp
	 *            comparator defining the order of keys in the priority queue
	 */
	public HeapPriorityQueue(Comparator<K> comp) {
		super(comp);
	}

	/**
	 * Creates a priority queue initialized with the respective key-value pairs. The
	 * two arrays given will be paired element-by-element. They are presumed to have
	 * the same length. (If not, entries will be created only up to the length of
	 * the shorter of the arrays)
	 * 
	 * @param keys
	 *            an array of the initial keys for the priority queue
	 * @param values
	 *            an array of the initial values for the priority queue
	 */
	public HeapPriorityQueue(K[] keys, V[] values) {
		super();
    for (int j = 0; j < Math.min(keys.length, values.length); j++)
			heap.add(new PQEntry<>(keys[j], values[j]));
		heapify();
	}

	// protected utilities
	protected int parent(int j) {
		return (j - 1) / 2;
	} // truncating division

	protected int left(int j) {
		return 2 * j + 1;
	}

	protected int right(int j) {
		return 2 * j + 2;
	}

	protected boolean hasLeft(int j) {
		return left(j) < heap.size();
	}

	protected boolean hasRight(int j) {
		return right(j) < heap.size();
	}

	/** Exchanges the entries at indices i and j of the array list. */
	protected void swap(int i, int j) {
		Entry<K, V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	/**
	 * Moves the entry at index j higher, if necessary, to restore the heap
	 * property.
	 */
	protected void upheap(int j) {
		while (j > 0) { // continue until reaching root (or break statement)
			int p = parent(j);
			if (compare(heap.get(j), heap.get(p)) >= 0)
				break; // heap property verified
			swap(j, p);
			j = p; // continue from the parent's location
		}
	}

	/**
	 * Moves the entry at index j lower, if necessary, to restore the heap property.
	 */
	protected void downheap(int j) {
		while (hasLeft(j)) { // continue to bottom (or break statement)
			int leftIndex = left(j);
			int smallChildIndex = leftIndex; // although right may be smaller
			if (hasRight(j)) {
				int rightIndex = right(j);
				if (compare(heap.get(leftIndex), heap.get(rightIndex)) > 0)
					smallChildIndex = rightIndex; // right child is smaller
			}
			if (compare(heap.get(smallChildIndex), heap.get(j)) >= 0)
				break; // heap property has been restored
			swap(j, smallChildIndex);
			j = smallChildIndex; // continue at position of the child
		}
	}

	/** Performs a bottom-up construction of the heap in linear time. */
	protected void heapify() {
		int startIndex = parent(size() - 1); // start at PARENT of last entry
		for (int j = startIndex; j >= 0; j--) // loop until processing the root
			downheap(j);
	}

	// public methods

	/**
	 * Returns the number of items in the priority queue.
	 * 
	 * @return number of items
	 */
	@Override
	public int size() {
		return heap.size();
	}

	/**
	 * Returns (but does not remove) an entry with minimal key.
	 * 
	 * @return entry having a minimal key (or null if empty)
	 */
	@Override
	public Entry<K, V> min() {
		if (heap.isEmpty())
			return null;
		return heap.get(0);
	}

	/**
	 * Inserts a key-value pair and return the entry created.
	 * 
	 * @param key
	 *            the key of the new entry
	 * @param value
	 *            the associated value of the new entry
	 * @return the entry storing the new key-value pair
	 * @throws IllegalArgumentException
	 *             if the key is unacceptable for this queue
	 */
	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		checkKey(key); // auxiliary key-checking method (could throw exception)
		Entry<K, V> newest = new PQEntry<>(key, value);
		heap.add(newest); // add to the end of the list
		upheap(heap.size() - 1); // upheap newly added entry
		return newest;
	}

	/**
	 * Removes and returns an entry with minimal key.
	 * 
	 * @return the removed entry (or null if empty)
	 */
	@Override
	public Entry<K, V> removeMin() {
		if (heap.isEmpty())
			return null;
		Entry<K, V> answer = heap.get(0);
		swap(0, heap.size() - 1); // put minimum item at the end
		heap.remove(heap.size() - 1); // and remove it from the list;
		downheap(0); // then fix new root
		return answer;
	}

	/** Used for debugging purposes only */
	private void sanityCheck() {
		for (int j = 0; j < heap.size(); j++) {
			int left = left(j);
			int right = right(j);
			if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0)
				System.out.println("Invalid left child relationship");
			if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0)
				System.out.println("Invalid right child relationship");
		}
	}

	@Override
	public int compare(Entry<K, V> o1, Entry<K, V> o2) {
		int sumO1 = 0, sumO2 = 0;

		for (int i = 0, j = 0; i < o1.getValue().toString().length()
				& j < o2.getValue().toString().length(); i++, j++) {
			sumO1 += o1.getValue().toString().charAt(i)-'0';
			sumO2 += o2.getValue().toString().charAt(j)-'0';
		}

		if (sumO1 == sumO2)
			return 0;

		return sumO1 - sumO2;
	}
	
	public int compareInt(K k, K k2) {
		if((Integer)k<=(Integer)k2) return 1;
		return -1;
	}

	public void printPreOrder(int index) {

		if (index >= heap.size()) {
			return;
		}
		for (int i = index; i > 0;) {
			i = (i - 1) / 2;
			System.out.print("*");
		}
		System.out.println(heap.get(index).getValue());
		printPreOrder((2 * index) + 1);
		printPreOrder((2 * index) + 2);

	}

	public void testHeapify() {
		Integer keys[] = { 1, 23, 10, 5 };
		Integer values[] = { 0, 36, 888, 99 };
		HeapPriorityQueue<Integer, Integer> myHeap = new HeapPriorityQueue<>(keys, values);
		System.out.println("Çalýþýyor...");
		myHeap.printPreOrder(keys.length);
	}

	public void lessThanOrEqual(K k) {
		lessThanOrEqual(k, 0);
	}

	public Entry<K,V> lessThanOrEqual(K k,int index){
			if(this.compareInt(heap.get(index).getKey(),k)<=0) {
				System.out.println("Value: "+ heap.get(index).getValue());
				if(this.hasLeft(index))
				return lessThanOrEqual(k, this.left(index));
				if(this.hasRight(index))
				return lessThanOrEqual(k, this.right(index));
			}
		return null;
}

	public static void main(String...strings ) {
		Integer key[] = { 4, 893, 100, 57 ,12,6};
		String value[] = { "10", "6", "88", "45","23","3 "};
		 HeapPriorityQueue<Integer, String> heap=new HeapPriorityQueue<>(key,value);
		 heap.printPreOrder(0);
		 heap.removeMin();
		 System.out.println("2.-------------");
		 heap.printPreOrder(0);
		 heap.removeMin();
		 System.out.println("3.---------------");
		 heap.printPreOrder(0);
		 heap.removeMin();
		 heap.insert(12, "87");
		 System.out.println("4.----------------");
		 heap.printPreOrder(0);
	}
}
