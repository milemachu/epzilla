/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.*;

public final class ReadOnlyList<E> implements List<E> {

    public static final String READONLY = "This list or iterator is read only.";

    private final Collection<E> _collection;

    public ReadOnlyList(Collection<E> collection) {
        _collection = collection;
    }

    public boolean add(E o) {
        throw new UnsupportedOperationException(READONLY);
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException(READONLY);
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException(READONLY);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException(READONLY);
    }

    public void clear() {
        throw new UnsupportedOperationException(READONLY);
    }

    public boolean contains(Object o) {
        return _collection.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return _collection.containsAll(c);
    }

    public E get(int index) {
        return ((List<E>) _collection).get(index);
    }

    public int indexOf(Object o) {
        return ((List<E>) _collection).indexOf(o);
    }

    public boolean isEmpty() {
        return _collection.isEmpty();
    }

    public Iterator<E> iterator() {
        return new ReadOnlyIterator<E>(_collection.iterator());
    }

    public int lastIndexOf(Object o) {
        return ((List<E>) _collection).lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return new ReadOnlyListIterator<E>(((List<E>) _collection).listIterator());
    }

    public ListIterator<E> listIterator(int index) {
        return new ReadOnlyListIterator<E>(((List<E>) _collection).listIterator(index));
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException(READONLY);
    }

    public E remove(int index) {
        throw new UnsupportedOperationException(READONLY);
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(READONLY);
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(READONLY);
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException(READONLY);
    }

    public int size() {
        return _collection.size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return new ReadOnlyList<E>(((List<E>) _collection).subList(fromIndex, toIndex));
    }

    public Object[] toArray() {
        return _collection.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return _collection.toArray(a);
    }
}
