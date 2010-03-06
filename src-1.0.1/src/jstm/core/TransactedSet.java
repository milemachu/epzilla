/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.*;

import jstm.misc.ReadOnlyIterator;
import jstm.misc.Utils;

/**
 * Performance notes: <br>
 * <br>
 * Some methods returning a value have a twin method with same name + Fast,
 * which does not return a value, e.g. <code>remove</code>. This is a
 * performance optimization: Retrieving previous element can be costly and can
 * invalidate the current transaction if another transaction updates this value
 * concurrently. If you do not need previous value, use the 'fast' method
 * instead.
 */
public class TransactedSet<E> extends TransactedObject implements Set<E> {

    protected HashMap<E, Object> _map = new HashMap<E, Object>();

    public TransactedSet() {
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be false. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>addFast</code>
     */
    public final boolean add(E key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).addFast(key);
            transaction.beginCommit(null);
            return false;
        }

        return getOrCreateVersion(transaction).add(key);
    }

    /**
     * @see class comment
     */
    public final void addFast(E key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).addFast(key);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be false. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>addAllFast</code>
     */
    public final boolean addAll(Collection<? extends E> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).addAllFast(c);
            transaction.beginCommit(null);
            return false;
        }

        return getOrCreateVersion(transaction).addAll(c);
    }

    /**
     * @see class comment
     */
    public final void addAllFast(Collection<? extends E> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).addAllFast(c);

        if (created)
            transaction.beginCommit(null);
    }

    public final void clear() {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).clear();

        if (created)
            transaction.beginCommit(null);
    }

    @SuppressWarnings("unchecked")
    public final boolean contains(Object o) {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.contains((E) o, true);

        return _map.containsKey(o);
    }

    @SuppressWarnings("unchecked")
    protected final boolean containsWithoutRecord(Transaction transaction, TransactedSetVersion<E> version, E key) {
        if (version != null)
            return version.contains(key, false);

        Object override = TransactedSetVersion.getDependenciesOverride(transaction, this, key);

        if (override != null) {
            if (override == Removal.Instance)
                return false;

            return true;
        }

        return _map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public final boolean containsAll(Collection<?> c) {
        Collection<? extends E> ce = (Collection<? extends E>) c;

        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.containsAll(ce);

        Iterator<? extends E> e = ce.iterator();

        while (e.hasNext())
            if (!_map.containsKey(e.next()))
                return false;

        return true;
    }

    public final boolean isEmpty() {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.isEmpty();

        return _map.isEmpty();
    }

    public final Iterator<E> iterator() {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return new ReadOnlyIterator<E>(version.keySet().iterator());

        return new ReadOnlyIterator<E>(_map.keySet().iterator());
    }

    @SuppressWarnings("unchecked")
    public final Collection<E> readWithoutRecord(Transaction transaction) {
        Collection<E> collection = null;

        if (transaction != null && transaction.getPrivateObjects() != null) {
            TransactedSetVersion<E> version = (TransactedSetVersion) transaction.getPrivateObjects().get(this);

            if (version != null)
                collection = version.keySet(false);
        }

        if (collection == null)
            collection = _map.keySet();

        return collection;
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be false. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>removeFast</code>
     */
    @SuppressWarnings("unchecked")
    public final boolean remove(Object key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).removeFast((E) key);
            transaction.beginCommit(null);
            return false;
        }

        return getOrCreateVersion(transaction).removeKey((E) key);
    }

    /**
     * @see class comment
     */
    @SuppressWarnings("unchecked")
    public final void removeFast(Object key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).removeFast((E) key);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be false. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>removeAllFast</code>
     */
    public final boolean removeAll(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).removeAllFast(c);
            transaction.beginCommit(null);
            return false;
        }

        return getOrCreateVersion(transaction).removeAll(c);
    }

    /**
     * @see class comment
     */
    @SuppressWarnings("unchecked")
    public final void removeAllFast(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).removeAllFast(c);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be false. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>removeAllFast</code>
     */
    public final boolean retainAll(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).retainAll(c, false);
            transaction.beginCommit(null);
            return false;
        }

        return getOrCreateVersion(transaction).retainAll(c, true);
    }

    /**
     * @see class comment
     */
    @SuppressWarnings("unchecked")
    public final void retainAllFast(Collection<?> c) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).retainAll(c, false);

        if (created)
            transaction.beginCommit(null);
    }

    public final int size() {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.size();

        return _map.size();
    }

    public final Object[] toArray() {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.toArray();

        return _map.keySet().toArray();
    }

    public final <T> T[] toArray(T[] a) {
        TransactedSetVersion<E> version = getVersion();

        if (version != null)
            return version.toArray(a);

        return Utils.copy(_map.keySet(), a);
    }

    // Listener

    public interface Listener<T> {

        void onAdded(Transaction transaction, T t);

        void onRemoved(Transaction transaction, T t);
    }

    public final void addListener(Listener<E> listener) {
        addListener(listener, listener);
    }

    protected final void addListener(Object key, Listener<E> listener) {
        Site.getLocal().addSiteListener(key, new SiteListener(listener));
    }

    public final void removeListener(Listener<E> listener) {
        removeListener((Object) listener);
    }

    protected final void removeListener(Object key) {
        Site.getLocal().removeTransactionListener(key);
    }

    private final class SiteListener implements Site.Listener {

        public final Listener Listener;

        public SiteListener(Listener listener) {
            Listener = listener;
        }

        @SuppressWarnings("unchecked")
        public void onCommitted(Transaction transaction) {
            if (transaction.getPrivateObjects() != null) {
                TransactedSetVersion version = (TransactedSetVersion) transaction.getPrivateObjects().get(TransactedSet.this);

                if (version != null)
                    version.raiseSetEvents(Listener);
            }
        }

        public void onAborted(Transaction transaction) {
        }
    }

    //

    @SuppressWarnings("unchecked")
    private final TransactedSetVersion<E> getVersion() {
        Transaction transaction = Transaction.getCurrent();

        if (transaction != null)
            return (TransactedSetVersion<E>) transaction.getVersion(this);

        return null;
    }

    @SuppressWarnings("unchecked")
    private final TransactedSetVersion<E> getOrCreateVersion(Transaction transaction) {
        return (TransactedSetVersion<E>) transaction.getVersion(this);
    }

    //

    @Override
    protected int getClassId() {
        return ObjectModel.Default.TRANSACTED_SET_CLASS_ID;
    }

    @Override
    protected TransactedObject.Version createVersion(Transaction transaction) {
        return new TransactedSetVersion<E>(this, transaction, _map, false);
    }

    @Override
    protected TransactedObject.Version getSnapshot(Transaction transaction) {
        return new TransactedSetVersion<E>(this, transaction, _map, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void serialize(TransactedObject.Version version, Writer writer) throws IOException {
        ((TransactedSetVersion<E>) version).serialize(writer);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void deserialize(TransactedObject.Version version, Reader reader) throws IOException {
        ((TransactedSetVersion<E>) version).deserialize(reader);
    }
}
