package CommonUtilsTest.UsefulObjects.CommonUtils;

import CommonUtils.Interfaces.MinHeapInterface;

import java.awt.*;
import java.util.Arrays;

public class MinHeap<E extends Comparable<E>> implements MinHeapInterface<E> {
    private E[] heap;  // Array to store heap elements
    private int size;  // Number of elements in the heap
    private final int DEFAULT_CAPACITY = 10;  // Default capacity of the heap

    @SuppressWarnings("unchecked")
    public MinHeap() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];  // Initialize array with default capacity
        size = 0;
    }

    /**
     * A recursive method to heapify (sort the root to where it should go) a
     * subtree with the root at given index.
     * Assumes the subtrees are already heapified.
     * (The purpose of this method is to balance tree starting at the root)
     *
     * @param i root of the subtree to heapify
     */
    private void heapify(int i) {
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        int smallest = i;

        // Compare left child
        if (leftChild < size && heap[leftChild].compareTo(heap[smallest]) < 0) {
            smallest = leftChild;
        }

        // Compare right child
        if (rightChild < size && heap[rightChild].compareTo(heap[smallest]) < 0) {
            smallest = rightChild;
        }

        // If the smallest is not the current node, swap and continue heapifying
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    /**
     * Swaps two elements in the heap.
     *
     * @param i index of the first element
     * @param j index of the second element
     */
    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Adds the item to the min heap.
     *
     * @param item item to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null element to the heap");
        }

        if (size == heap.length) {
            expandCapacity();
        }

        heap[size] = item;
        size++;

        // Percolate up to maintain heap property
        int i = size - 1;
        while (i > 0 && heap[i].compareTo(heap[parent(i)]) < 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Returns the index of the parent of the node at index i.
     *
     * @param i index of the node
     * @return index of the parent
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Expands the capacity of the heap when it's full.
     */
    private void expandCapacity() {
        heap = Arrays.copyOf(heap, heap.length * 2);
    }

    /**
     * Empties the heap.
     */
    @Override
    public void clear() {
        Arrays.fill(heap, null);
        size = 0;
    }

    /**
     * Returns the minimum element without removing it, or returns <code>null</code> if the heap is empty.
     *
     * @return the minimum element in the heap, or <code>null</code> if heap is empty
     */
    @Override
    public E peekMin() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    /**
     * Remove and return the minimum element in the heap, or returns <code>null</code> if heap is empty.
     *
     * @return the minimum element in the heap, or <code>null</code> if heap is empty
     */
    @Override
    public E removeMin() {
        if (size == 0) {
            return null;
        }

        E min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);  // Restore the heap property
        return min;
    }

    /**
     * Returns the number of elements in the heap.
     *
     * @return integer representing the number of elements in the heap
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void draw(Graphics g) {

    }
}
