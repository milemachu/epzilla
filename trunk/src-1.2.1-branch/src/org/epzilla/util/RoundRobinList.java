package org.epzilla.util;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 23, 2010
 * Time: 9:11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundRobinList<E> {

    private LinkedList<E> list = new LinkedList<E>();
    private int size = 0;
    private int current = 0;

    public synchronized void add(E item) {
        list.add(item);
        size++;
    }

    public int size() {
        return this.size;
    }

    public synchronized boolean remove(E item) {
        boolean success = list.remove(item);
        if (success) {
            size--;
        }
        return success;
    }

    public synchronized E next() {
        if (size == 0) {
            return null;
        }
        if (current > (size - 1)) {
            current = 0;
        }
        return this.list.get(current++);
    }

    public synchronized String toString() {
        return list.toString();
    }

}
