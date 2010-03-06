/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.ListIterator;

public final class ReadOnlyListIterator<T> implements ListIterator<T> {

    private final ListIterator<T> _iterator;

    public ReadOnlyListIterator(ListIterator<T> iterator) {
        _iterator = iterator;
    }

    public void add(T o) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public boolean hasNext() {
        return _iterator.hasNext();
    }

    public boolean hasPrevious() {
        return _iterator.hasPrevious();
    }

    public T next() {
        return _iterator.next();
    }

    public int nextIndex() {
        return _iterator.nextIndex();
    }

    public T previous() {
        return _iterator.previous();
    }

    public int previousIndex() {
        return _iterator.previousIndex();
    }

    public void remove() {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public void set(T o) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }
}
