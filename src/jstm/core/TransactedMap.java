/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.*;

import jstm.misc.ReadOnlyList;
import jstm.misc.ReadOnlySet;

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
public final class TransactedMap<K, V> extends TransactedObject implements Map<K, V> {

    protected HashMap<K, V> _map = new HashMap<K, V>();

    public TransactedMap() {
    }

    public void clear() {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).clear();

        if (created)
            transaction.beginCommit(null);
    }

    @SuppressWarnings("unchecked")
    public boolean containsKey(Object key) {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return version.contains((K) key);

        return _map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return version.containsValue((V) value);

        return _map.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    public Set<Map.Entry<K, V>> entrySet() {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return new ReadOnlySet<Map.Entry<K, V>>((Set) version.entrySet());

        return new ReadOnlySet<Map.Entry<K, V>>(_map.entrySet());
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return version.get((K) key);

        return _map.get(key);
    }

    public boolean isEmpty() {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return version.isEmpty();

        return _map.isEmpty();
    }

    public Set<K> keySet() {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return new ReadOnlySet<K>(version.keySet());

        return new ReadOnlySet<K>(_map.keySet());
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be null. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>putFast</code>
     */
    public V put(K key, V value) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).putFast(key, value);
            transaction.beginCommit(null);
            return null;
        }

        return getOrCreateVersion(transaction).put(key, value);
    }

    /**
     * @see class comment
     */
    public void putFast(K key, V value) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).putFast(key, value);

        if (created)
            transaction.beginCommit(null);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).putAll(t);

        if (created)
            transaction.beginCommit(null);
    }

    /**
     * If this method is not called in the context of a transaction the return
     * value will always be null. See TransactedObject comments.
     * 
     * @see <code>TransactedObject</code>
     * @see <code>removeFast</code>
     */
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        if (created) {
            getOrCreateVersion(transaction).removeFast((K) key);
            transaction.beginCommit(null);
            return null;
        }

        return getOrCreateVersion(transaction).remove((K) key);
    }

    /**
     * @see class comment
     */
    @SuppressWarnings("unchecked")
    public void removeFast(Object key) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        getOrCreateVersion(transaction).removeFast((K) key);

        if (created)
            transaction.beginCommit(null);
    }

    public int size() {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return version.size();

        return _map.size();
    }

    public Collection<V> values() {
        TransactedMapVersion<K, V> version = getVersion();

        if (version != null)
            return new ReadOnlyList<V>(version.values());

        return new ReadOnlyList<V>(_map.values());
    }

    // Listener

    public interface Listener<K, V> {

        void onPut(Transaction transaction, K k, V v);

        void onRemoved(Transaction transaction, K k);
    }

    public void addListener(Listener<K, V> listener) {
        addListener(listener, listener);
    }

    protected void addListener(Object key, Listener<K, V> listener) {
        Site.getLocal().addSiteListener(key, new SiteListener(listener));
    }

    public void removeListener(Listener<K, V> listener) {
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
                TransactedMapVersion version = (TransactedMapVersion) transaction.getPrivateObjects().get(TransactedMap.this);

                if (version != null)
                    version.raiseMapEvents(Listener);
            }
        }

        public void onAborted(Transaction transaction) {
        }
    }

    //

    @SuppressWarnings("unchecked")
    private TransactedMapVersion<K, V> getVersion() {
        Transaction transaction = Transaction.getCurrent();

        if (transaction != null)
            return (TransactedMapVersion<K, V>) transaction.getVersion(this);

        return null;
    }

    @SuppressWarnings("unchecked")
    private TransactedMapVersion<K, V> getOrCreateVersion(Transaction transaction) {
        return (TransactedMapVersion<K, V>) transaction.getVersion(this);
    }

    //

    @Override
    protected int getClassId() {
        return ObjectModel.Default.TRANSACTED_MAP_CLASS_ID;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TransactedObject.Version createVersion(Transaction transaction) {
        return new TransactedMapVersion<K, V>(this, transaction, (HashMap) _map, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TransactedObject.Version getSnapshot(Transaction transaction) {
        return new TransactedMapVersion<K, V>(this, transaction, (HashMap) _map, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void serialize(TransactedObject.Version version, Writer writer) throws IOException {
        ((TransactedMapVersion<K, V>) version).serialize(writer);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void deserialize(TransactedObject.Version version, Reader reader) throws IOException {
        ((TransactedMapVersion<K, V>) version).deserialize(reader);
    }
}
