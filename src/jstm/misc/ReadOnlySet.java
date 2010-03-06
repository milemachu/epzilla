/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public final class ReadOnlySet<E> implements Set<E> {

    private final Set<E> _set;

    public ReadOnlySet(Set<E> set) {
        _set = set;
    }

    public boolean add(E o) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public void clear() {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public boolean contains(Object o) {
        return _set.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return _set.containsAll(c);
    }

    public boolean isEmpty() {
        return _set.isEmpty();
    }

    public Iterator<E> iterator() {
        return new ReadOnlyIterator<E>(_set.iterator());
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }

    public int size() {
        return _set.size();
    }

    public Object[] toArray() {
        return _set.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return Utils.copy(_set, a);
    }
}
