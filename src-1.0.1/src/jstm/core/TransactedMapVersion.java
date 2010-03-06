/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.*;
import java.util.Map.Entry;

import jstm.core.TransactedObject.Removal;

final class TransactedMapVersion<K, V> extends TransactedSetVersion<K> {

    @SuppressWarnings("unchecked")
    public TransactedMapVersion(TransactedObject shared, Transaction transaction, HashMap<K, Object> sharedMap, boolean snapshot) {
        super(shared, transaction, sharedMap, snapshot);
    }

    public boolean containsValue(V value) {
        startFullRead(true);

        if (getPrivateMap() != null)
            return getPrivateMap().containsValue(value);

        return getSharedMap().containsValue(value);
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        Object override = getSpecificVersionOverride(key);

        if (override != null)
            return (V) getValueFromOverride(override);

        recordRead(key);

        if (getPrivateMap() != null)
            return (V) getPrivateMap().get(key);

        return (V) getPrevious(getTransaction(), key);
    }

    public V put(K key, V value) {
        V previous = get(key);
        putFast(key, value);
        return previous;
    }

    public void putFast(K key, V value) {
        if (getPrivateMap() != null)
            getPrivateMap().put(key, value);

        getOrCreateWrites().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> t) {
        Iterator it = t.entrySet().iterator();

        while (it.hasNext()) {
            Entry<K, V> entry = (Entry<K, V>) it.next();
            putFast(entry.getKey(), entry.getValue());
        }
    }

    public V remove(K key) {
        V previous = get(key);
        removeFast(key);
        return previous;
    }

    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        startFullRead(true);

        if (getPrivateMap() != null)
            return (Collection<V>) getPrivateMap().values();

        return (Collection) getSharedMap().values();
    }

    //

    @SuppressWarnings("unchecked")
    @Override
    protected void replaceSharedMapBy(HashMap<K, Object> map) {
        if (getSharedObject() instanceof TransactedMap)
            ((TransactedMap) getSharedObject())._map = map;
        else
            throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    protected void raiseMapEvents(TransactedMap.Listener<K, V> listener) {
        if (getWrites() != null) {
            for (Map.Entry<K, Object> entry : getWrites().entrySet()) {
                if (entry.getValue() != Removal.Instance)
                    listener.onPut(getTransaction(), entry.getKey(), (V) entry.getValue());
                else
                    listener.onRemoved(getTransaction(), entry.getKey());
            }
        }
    }
}