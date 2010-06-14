package org.epzilla.util;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 23, 2010
 * Time: 1:48:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CircularList<E> {

    private Node start = null;
    private Node end = null;
    private Node curr = null;
    private int size = 0;

    private class Node<E> {
        Node<E> next = null;
        E content = null;
    }

    public int size() {
        return this.size;
    }

    public synchronized boolean remove(E item) {
        Node t = start;
        Node pre = end;
        boolean success = false;
        if (item != null) {
            do {
                if (t != null) {
                    if (item.equals(t.content)) {
                        if (curr == t) {
                            curr = t.next;
                        }
                        if (start == t) {
                            start = t.next;
                        }
                        if (end == t) {
                            end = pre;
                        }
                        pre.next = t.next;
                        size--;
                        success = true;
                        break;
                    }
                } else {
                    break;
                }
                pre = t;
                t = t.next;
            } while (t != start);
        }
        if (size == 0) {
            start = null;
            end = null;
            curr = null;
        }
        return success;
    }


    /**
     * Removes all items equal to 'item'. Looks at content. not reference.
     * @param item  item to compare with.
     * @return  true if at least one element is removed.
     */
    public synchronized boolean removeAll(E item) {
        boolean s = false;
        while (remove(item)) {
            s = true;
        }
        return s;
    }

    /**
     * adds the 'item' to the end of the list.
     * @param item
     */
    public synchronized void add(E item) {
        Node<E> n = new Node<E>();
        n.content = item;
        if (size == 0) {
            start = n;
            end = n;
            n.next = n;
            curr = n;
        } else {
            end.next = n;
            end = n;
            n.next = start;
        }
        size++;
    }

    /**
     * next element in the list. when end is reached, this will return the starting element again and the loop
     * will continue.
     * @return
     */
    public synchronized E next() {
        if (size == 0) {
            return null;
        }
        E e = (E) curr.content;
        curr = curr.next;
        return e;
    }


    public synchronized  String toString() {
        Node c = start;
        StringBuilder sb = new StringBuilder("CircularList: ");
        for (int i = 0; i< size; i++) {
            sb.append(c.content);
            c = c.next;
            sb.append(", ");

        }
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularList<Integer> lis = new CircularList();
        for (int i = 0; i < 8; i++) {
            lis.add(i);
        }
//        lis.remove(2);
        System.out.println("size: " + lis.size);
        for (int i = 0; i < lis.size; i++) {
            System.out.println(lis.next());
        }
//        lis.add(4);
//        lis.add(4);
        System.out.println("operations..........");

//        lis.add(41);
//        lis.remove(4);
//        lis.add(43);
//        lis.remove(4);
        System.out.println("size: " + lis.size);


        for (int i = 0; i < lis.size; i++) {
            System.out.println(lis.next());
        }
        System.out.println("new ops...");
        for (int i = 0; i < 15; i++) {
            System.out.println(lis.next());
            System.out.println(lis.remove(i));
//           lis.add(i);
        }
        lis.add(11);
        lis.add(11);
        lis.add(11);
        lis.add(11);
        System.out.println(lis.toString());
                  lis.removeAll(11);
        System.out.println(lis.toString());

    }


}
