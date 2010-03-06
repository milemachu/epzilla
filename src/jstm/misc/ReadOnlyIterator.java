/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.Iterator;

public final class ReadOnlyIterator<T> implements Iterator<T> {

    private final Iterator<T> _iterator;

    public ReadOnlyIterator(Iterator<T> iterator) {
        _iterator = iterator;
    }

    public boolean hasNext() {
        return _iterator.hasNext();
    }

    public T next() {
        return _iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException(ReadOnlyList.READONLY);
    }
}
