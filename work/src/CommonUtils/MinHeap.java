package CommonUtils;

import CommonUtils.Interfaces.MinHeapInterface;

import java.awt.*;

/**
 * Implements our MinHeapInterface and adds a constructor
 * <p>
 * <b>251 students: You are explicitly forbidden from using java.util.Queue (including any subclass
 *   like PriorityQueue) and any other java.util.* library EXCEPT java.util.Arrays and java.util.Vector.
 *   Write your own implementation of a MinHeap.</b>
 *
 * @param <E> the type of object this heap will be holding
 */
public class MinHeap<E extends Comparable<E>> implements MinHeapInterface<E> {
    /**
     * A recursive method to heapify (sort the root to where it should go) a
     *   subtree with the root at given index
     * Assumes the subtrees are already heapified.
     * (The purpose of this method is to balance tree starting at the root)
     * @param i root of the subtree to heapify
     */
    private void heapify(int i) {
        //todo
    }

    /**
     * Constructs an empty min heap
     */
    public MinHeap(){
        //todo
    }

    /**
     * Adds the item to the min heap
     *
     * @param item item to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E item) {
        //todo
    }

    /**
     * Empties the heap.
     */
    @Override
    public void clear() {
        //todo
    }

    /**
     * Returns the minimum element without removing it, or returns <code>null</code> if heap is empty
     *
     * @return the minimum element in the heap, or <code>null</code> if heap is empty
     */
    @Override
    public E peekMin() {
        //todo
        return null;
    }

    /**
     * Remove and return the minimum element in the heap, or returns <code>null</code> if heap is empty
     *
     * @return the minimum element in the heap, or <code>null</code> if heap is empty
     */
    @Override
    public E removeMin() {
        //todo
        return null;
    }

    /**
     * Returns the number of elements in the heap
     *
     * @return integer representing the number of elements in the heap
     */
    @Override
    public int size() {
        //todo
        return -1;
    }

    /**
     * DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
     *
     * @param g graphics object to draw on
     */
    @Override
    public void draw(Graphics g) {
        //DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
        if(g != null) g.getColor();
        //todo GRAPHICS DEVELOPER:: draw the MinHeap how we discussed
        //251 STUDENTS:: YOU ARE NOT THE GRAPHICS DEVELOPER!
    }
}
