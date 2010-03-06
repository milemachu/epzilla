/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.AbstractList;

/**
 * A unbounded circular queue.
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version modified for jstm
 */
public final class Queue<E> extends AbstractList<E> {

    private static final int DEFAULT_CAPACITY = 4;

    private static final int DEFAULT_MASK = DEFAULT_CAPACITY - 1;

    private E[] items;

    private int mask;

    private int first = 0;

    private int last = 0;

    private int size = 0;

    /**
     * Construct a new, empty queue.
     */
    @SuppressWarnings("unchecked")
    public Queue() {
        items = (E[]) new Object[DEFAULT_CAPACITY];
        mask = DEFAULT_MASK;
    }

    /**
     * Clears this queue.
     */
    @Override
    public void clear() {
        for (int i = 0; i < items.length; i++)
            items[i] = null;

        first = 0;
        last = 0;
        size = 0;
    }

    /**
     * Dequeues from this queue.
     * 
     * @return <code>null</code>, if this queue is empty or the element is
     *         really <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public E pop() {
        if (size == 0) {
            return null;
        }

        E ret = items[first];
        items[first] = null;
        decreaseSize();

        return ret;
    }

    /**
     * Enqueue into this queue.
     */
    public void push(E item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        ensureCapacity();
        items[last] = item;
        increaseSize();
    }

    /**
     * Returns the first element of the queue.
     * 
     * @return <code>null</code>, if the queue is empty, or the element is
     *         really <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public E first() {
        if (size == 0) {
            return null;
        }

        return items[first];
    }

    /**
     * Returns the last element of the queue.
     * 
     * @return <code>null</code>, if the queue is empty, or the element is
     *         really <code>null</code>.
     */
    public Object last() {
        if (size == 0) {
            return null;
        }

        return items[(last + items.length - 1) & mask];
    }

    @Override
    public E get(int idx) {
        checkIndex(idx);
        return items[getRealIndex(idx)];
    }

    /**
     * Returns <code>true</code> if the queue is empty.
     */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns the number of elements in the queue.
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "first=" + first + ", last=" + last + ", size=" + size + ", mask = " + mask;
    }

    private void checkIndex(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(idx));
        }
    }

    private int getRealIndex(int idx) {
        return (first + idx) & mask;
    }

    private void increaseSize() {
        last = (last + 1) & mask;
        size++;
    }

    private void decreaseSize() {
        first = (first + 1) & mask;
        size--;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < items.length) {
            return;
        }

        // expand queue
        final int oldLen = items.length;
        E[] tmp = (E[]) new Object[oldLen * 2];

        if (first < last) {
            System.arraycopy(items, first, tmp, 0, last - first);
        } else {
            System.arraycopy(items, first, tmp, 0, oldLen - first);
            System.arraycopy(items, 0, tmp, oldLen - first, last);
        }

        first = 0;
        last = oldLen;
        items = tmp;
        mask = tmp.length - 1;
    }
}