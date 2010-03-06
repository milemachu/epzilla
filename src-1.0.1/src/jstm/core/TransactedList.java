/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.*;

import jstm.misc.*;

/**
 * Performance notes: <br>
 * <br>
 * If elements are removed often from the list, it is faster to use a
 * TransactedSet instead, as it does not need to replicate the shift of the
 * elements following the removed one. <br>
 * <br>
 * Some methods returning a value have a twin method with same name + Fast,
 * which does not return a value, e.g. <code>remove</code>. This is a
 * performance optimization: Retrieving previous element can be costly and can
 * invalidate the current transaction if another transaction updates this value
 * concurrently. If you do not need previous value, use the 'fast' method
 * instead. <br>
 * <br>
 * It is faster to use the iterator than to iterate using for(int i = 0;... The
 * iterator has to get the current transaction and list version once, whereas
 * the for version will do it for each call to size() and get(i).
 */
public final class TransactedList<E> extends TransactedRandomAccess<E> implements List<E> {

    protected int _size;

    private ListImpl<E> _listImpl;

    //Added by dishan
    private String name;

    public TransactedList() {
        super(10);
    }

    public TransactedList(int initialCapacity) {
        super(initialCapacity);
    }

    public TransactedList(Collection<? extends E> c) {
        // From ArrayList
        super((int) Math.min((c.size() * 110L) / 100, Integer.MAX_VALUE));

        addAll(c);
    }

    // TODO do a special case for add so it does not invalidate current
    // transaction if size is changed concurrently.

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean add(E o) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction, true).getPrivateList().add(o);

        if (created)
            transaction.beginCommit(null);

        return true;
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public void add(int index, E element) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction, true).getPrivateList().add(index, element);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean addAll(Collection<? extends E> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        boolean result = getOrCreateVersion(transaction, true).getPrivateList().addAll(c);

        if (created)
            transaction.beginCommit(null);

        return result;
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        boolean result = getOrCreateVersion(transaction, true).getPrivateList().addAll(index, c);

        if (created)
            transaction.beginCommit(null);

        return result;
    }

    public void clear() {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction, false).getPrivateList().clear();

        if (created)
            transaction.beginCommit(null);
    }

    public boolean contains(Object o) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().contains(o);

        return getListImpl().contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().containsAll(c);

        return getListImpl().containsAll(c);
    }

    public E get(int index) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, false);

        if (version != null)
            return version.getPrivateList().get(index);

        return getListImpl().get(index);
    }

    public int indexOf(Object o) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().indexOf(o);

        return getListImpl().indexOf(o);
    }

    public boolean isEmpty() {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().isEmpty();

        return getListImpl().isEmpty();
    }

    public Iterator<E> iterator() {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().iterator();

        return new ReadOnlyIterator<E>(getListImpl().iterator());
    }

    public int lastIndexOf(Object o) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().lastIndexOf(o);

        return getListImpl().lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().listIterator();

        return new ReadOnlyListIterator<E>(getListImpl().listIterator());
    }

    public ListIterator<E> listIterator(int index) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().listIterator(index);

        return new ReadOnlyListIterator<E>(getListImpl().listIterator(index));
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean remove(Object o) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        boolean result = getOrCreateVersion(transaction, true).getPrivateList().remove(o);

        if (created)
            transaction.beginCommit(null);

        return result;
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be null. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>removeFast</code>
     */
    public E remove(int index) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction, false).getPrivateList().removeFast(index);
            transaction.beginCommit(null);
            return null;
        }

        return getOrCreateVersion(transaction, false).getPrivateList().remove(index);
    }

    /**
     * @see class comment
     */
    public void removeFast(int index) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction, false).getPrivateList().removeFast(index);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean removeAll(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        boolean result = getOrCreateVersion(transaction, true).getPrivateList().removeAll(c);

        if (created)
            transaction.beginCommit(null);

        return result;
    }

    /**
     * If this method is not called in the context of a transaction, the list
     * must not be modified concurrently by another transaction or the effects
     * of the method call could be aborted.
     */
    public boolean retainAll(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        boolean result = getOrCreateVersion(transaction, true).getPrivateList().retainAll(c);

        if (created)
            transaction.beginCommit(null);

        return result;
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be null. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>setFast</code>
     */
    public E set(int index, E value) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction, false).getPrivateList().setFast(index, value);
            transaction.beginCommit(null);
            return null;
        }

        return getOrCreateVersion(transaction, false).getPrivateList().set(index, value);
    }

    /**
     * @see class comment
     */
    public void setFast(int index, E value) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction, false).getPrivateList().setFast(index, value);

        if (created)
            transaction.beginCommit(null);
    }

    public int size() {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().size();

        return getListImpl().size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().subList(fromIndex, toIndex);

        return new ReadOnlyList<E>(getListImpl().subList(fromIndex, toIndex));
    }

    public Object[] toArray() {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().toArray();

        return getListImpl().toArray();
    }

    public <T> T[] toArray(T[] a) {
        Transaction transaction = Transaction.getCurrent();
        TransactedListVersion<E> version = getVersion(transaction, true);

        if (version != null)
            return version.getPrivateList().toArray(a);

        return getListImpl().toArray(a);
    }

    //

    private ListImpl<E> getListImpl() {
        if (_listImpl == null)
            _listImpl = new ListImpl<E>(this);

        return _listImpl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static final class ListImpl<T> extends AbstractList<T> implements RandomAccess {

        private final T[] _fields;

        private final int _size;

        public ListImpl(TransactedList<T> list) {
            _fields = list._fields;
            _size = list._size;
        }

        @Override
        public T get(int index) {
            if (index >= _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            return _fields[index];
        }

        @Override
        public int size() {
            return _size;
        }
    }

    @SuppressWarnings("unchecked")
    public void resize(int value) {
        if (value != _size) {
            // Copy array if bigger

            int oldCapacity = _fields.length;

            if (value > oldCapacity) {
                int newCapacity = (oldCapacity * 3) / 2 + 1;

                if (newCapacity < value)
                    newCapacity = value;

                Object[] oldArray = _fields;
                _fields = (E[]) new Object[newCapacity];
                Utils.copy(oldArray, 0, _fields, 0, _size);
            }

            // Null elements if smaller

            for (int i = value; i < _size; i++)
                _fields[i] = null;

            // Update

            _size = value;

            // Invalidate ListImpl

            _listImpl = null;
        }
    }

    // Listener

    public void addSizeListener(SizeListener listener) {
        addSizeListener(listener, listener);
    }

    protected void addSizeListener(Object key, SizeListener listener) {
        Site.getLocal().addSiteListener(key, new SiteListener(listener));
    }

    public void removeSizeListener(SizeListener listener) {
        removeSizeListener((Object) listener);
    }

    protected void removeSizeListener(Object key) {
        Site.getLocal().removeTransactionListener(key);
    }

    private final class SiteListener implements Site.Listener {

        public final SizeListener Listener;

        public SiteListener(SizeListener listener) {
            Listener = listener;
        }

        @SuppressWarnings("unchecked")
        public void onCommitted(Transaction transaction) {
            if (transaction.getPrivateObjects() != null) {
                TransactedListVersion version = (TransactedListVersion) transaction.getPrivateObjects().get(TransactedList.this);

                if (version != null)
                    version.raiseSizeEvents(Listener);
            }
        }

        public void onAborted(Transaction transaction) {
        }
    }

    //

    @SuppressWarnings("unchecked")
    private TransactedListVersion<E> getVersion(Transaction transaction, boolean abortIfSizeChanged) {
        if (transaction != null) {
            TransactedListVersion<E> version = (TransactedListVersion<E>) transaction.getVersion(this);

            if (version != null) {
                if (abortIfSizeChanged)
                    version.abortIfSizeChanged();

                return version;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private TransactedListVersion<E> getOrCreateVersion(Transaction transaction, boolean abortIfSizeChanged) {
        TransactedListVersion<E> version = (TransactedListVersion<E>) transaction.getVersion(this);

        if (abortIfSizeChanged)
            version.abortIfSizeChanged();

        return version;
    }

    //

    @Override
    protected int getClassId() {
        return ObjectModel.Default.TRANSACTED_LIST_CLASS_ID;
    }

    @Override
    protected TransactedObject.Version createVersion(Transaction transaction) {
        return new TransactedListVersion<E>(transaction, this, false);
    }

    @Override
    protected TransactedObject.Version getSnapshot(Transaction transaction) {
        return new TransactedListVersion<E>(transaction, this, true);
    }

    @Override
    protected void serialize(TransactedObject.Version baseVersion, Writer writer) throws IOException {
        TransactedListVersion version = (TransactedListVersion) baseVersion;
        version.serialize(writer);
    }

    @Override
    protected void deserialize(TransactedObject.Version baseVersion, Reader reader) throws IOException {
        TransactedListVersion version = (TransactedListVersion) baseVersion;
        version.deserialize(reader);
    }
}