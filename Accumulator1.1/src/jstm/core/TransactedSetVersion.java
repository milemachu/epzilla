/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import jstm.core.TransactedObject.Reference;
import jstm.core.TransactedObject.ReferencesWalker;
import jstm.core.TransactedObject.Removal;
import jstm.misc.Debug;
import jstm.misc.Utils;

class TransactedSetVersion<E> extends TransactedObject.Version {

    protected static final Object ENTRIES_VALUE = new Object();

    private final Transaction _transaction;

    private HashMap<E, Object> _sharedMap;

    private HashSet<E> _reads;

    private HashMap<E, Object> _writes;

    private boolean _fullyRead, _cleared;

    private HashMap<E, Object> _privateMap;

    public TransactedSetVersion(TransactedObject shared, Transaction transaction, HashMap<E, Object> sharedMap, boolean snapshot) {
        super(shared);

        if (sharedMap == null || transaction == null)
            throw new IllegalArgumentException();

        _transaction = transaction;
        _sharedMap = sharedMap;

        if (snapshot && sharedMap.size() > 0)
            _writes = sharedMap;
    }

    public final Transaction getTransaction() {
        return _transaction;
    }

    protected final HashMap<E, Object> getSharedMap() {
        return _sharedMap;
    }

    protected final HashMap<E, Object> getPrivateMap() {
        return _privateMap;
    }

    protected final HashMap<E, Object> getWrites() {
        return _writes;
    }

    protected final HashMap<E, Object> getOrCreateWrites() {
        if (_writes == null)
            _writes = new HashMap<E, Object>();

        markTransactionIfModifiesTopology();

        return _writes;
    }

    //

    public final boolean add(E key) {
        if (contains(key))
            return false;
        else {
            addFast(key);
            return true;
        }
    }

    public final void addFast(E key) {
        if (getPrivateMap() != null)
            getPrivateMap().put(key, ENTRIES_VALUE);

        put(key, ENTRIES_VALUE);
    }

    public final boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        Iterator<? extends E> e = c.iterator();

        while (e.hasNext())
            if (add(e.next()))
                modified = true;

        return modified;
    }

    public final void addAllFast(Collection<? extends E> c) {
        Iterator<? extends E> e = c.iterator();

        while (e.hasNext())
            addFast(e.next());
    }

    public final void clear() {
        _writes = null;
        _cleared = true;

        if (_privateMap != null)
            _privateMap.clear();

        markTransactionIfModifiesTopology();

        if (getTransaction().hasListeners())
            getTransaction().raiseNewChange(new Change.Clear(getTransaction(), this));
    }

    protected void cancelClear() {
        _cleared = false;
    }

    public final boolean contains(E key) {
        return contains(key, true);
    }

    protected final boolean contains(E key, boolean recordRead) {
        Object override = getSpecificVersionOverride(key);

        if (override != null) {
            if (override == TransactedObject.Removal.Instance)
                return false;

            return true;
        }

        if (recordRead)
            recordRead(key);

        if (_privateMap != null)
            return _privateMap.containsKey(key);

        override = getDependenciesOverride(getTransaction(), getSharedObject(), key);

        if (override != null) {
            if (override == TransactedObject.Removal.Instance)
                return false;

            return true;
        }

        return _sharedMap.containsKey(key);
    }

    public final boolean containsAll(Collection<? extends E> c) {
        Iterator<? extends E> e = c.iterator();

        while (e.hasNext())
            if (!contains(e.next()))
                return false;

        return true;
    }

    public final Set<Map.Entry<E, Object>> entrySet() {
        startFullRead(true);

        if (_privateMap != null)
            return _privateMap.entrySet();

        return _sharedMap.entrySet();
    }

    public final boolean isEmpty() {
        startFullRead(true);

        if (_privateMap != null)
            return _privateMap.isEmpty();

        return _sharedMap.isEmpty();
    }

    public final Set<E> keySet() {
        return keySet(true);
    }

    protected final Set<E> keySet(boolean record) {
        startFullRead(record);

        if (_privateMap != null)
            return _privateMap.keySet();

        return _sharedMap.keySet();
    }

    public final boolean removeKey(E key) {
        boolean contains = contains(key);
        removeFast(key);
        return contains;
    }

    public final void removeFast(E key) {
        if (_privateMap != null)
            _privateMap.remove(key);

        put(key, Removal.Instance);
    }

    @SuppressWarnings("unchecked")
    public final boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (Object o : c)
            if (removeKey((E) o))
                modified = true;

        return modified;
    }

    @SuppressWarnings("unchecked")
    public final void removeAllFast(Collection<?> c) {
        for (Object o : c)
            removeFast((E) o);
    }

    public final boolean retainAll(Collection<?> c, boolean record) {
        ArrayList<E> list = new ArrayList<E>();

        startFullRead(record);

        for (E e : _privateMap.keySet())
            if (!c.contains(e))
                list.add(e);

        for (E e : list)
            removeFast(e);

        return list.size() > 0;
    }

    public final int size() {
        startFullRead(true);

        if (_privateMap != null)
            return _privateMap.size();

        return _sharedMap.size();
    }

    public final Object[] toArray() {
        startFullRead(true);

        if (_privateMap != null)
            return _privateMap.keySet().toArray();

        return _sharedMap.keySet().toArray();
    }

    public final <T> T[] toArray(T[] a) {
        startFullRead(true);

        if (_privateMap != null)
            return Utils.copy(_privateMap.keySet(), a);

        return Utils.copy(_sharedMap.keySet(), a);
    }

    //

    private void put(E key, Object value) {
        getOrCreateWrites().put(key, value);

        if (getTransaction().hasListeners())
            getTransaction().raiseNewChange(new Change.Set(getTransaction(), this, key));
    }

    protected final Object getSpecificVersionOverride(E key) {
        if (_writes != null) {
            Object override = _writes.get(key);

            if (override != null)
                return override;
        }

        if (_cleared)
            return TransactedObject.Removal.Instance;

        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object getDependenciesOverride(Transaction transaction, TransactedObject shared, Object key) {
        if (transaction.getPotentialDependencies() != null) {
            for (Transaction dependency : transaction.getPotentialDependencies()) {
                if (dependency.getPrivateObjects() != null) {
                    TransactedSetVersion parent = (TransactedSetVersion) dependency.getPrivateObjects().get(shared);

                    if (parent != null) {
                        Object override = parent.getSpecificVersionOverride(key);

                        if (override != null) {
                            transaction.confirmDependency(dependency);
                            return override;
                        }
                    }
                }
            }
        }

        return null;
    }

    protected final Object getPrevious(Transaction transaction, Object key) {
        Object override = getDependenciesOverride(getTransaction(), getSharedObject(), key);

        if (override != null)
            return getValueFromOverride(override);

        return getSharedMap().get(key);
    }

    @SuppressWarnings("unchecked")
    protected final Object getValueFromOverride(Object override) {
        if (override == TransactedObject.Removal.Instance)
            return null;

        return override;
    }

    public final void recordRead(E key) {
        Debug.assertion(key != null);

        if (getTransaction().getStatus() == Transaction.Status.RUNNING) {
            if (_reads == null)
                _reads = new HashSet<E>();

            _reads.add(key);
        }
    }

    protected final void startFullRead(boolean record) {
        if (record && getTransaction().getStatus() == Transaction.Status.RUNNING)
            _fullyRead = true;

        if (_privateMap == null) {
            ArrayList<TransactedSetVersion<E>> walked = new ArrayList<TransactedSetVersion<E>>();
            HashMap<E, Object> parent = getParentMap(walked);

            if (_cleared || _writes != null)
                walked.add(this);

            if (walked.size() > 0) {
                _privateMap = new HashMap<E, Object>(parent);

                for (int i = 0; i < walked.size(); i++) {
                    TransactedSetVersion<E> version = walked.get(i);
                    version.runWritesOn(_privateMap);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private final HashMap<E, Object> getParentMap(ArrayList<TransactedSetVersion<E>> walked) {
        if (getTransaction().getPotentialDependencies() != null) {
            for (Transaction dependency : getTransaction().getPotentialDependencies()) {
                if (dependency.getPrivateObjects() != null) {
                    TransactedSetVersion<E> parent = (TransactedSetVersion<E>) dependency.getPrivateObjects().get(getSharedObject());

                    if (parent != null) {
                        getTransaction().confirmDependency(dependency);

                        if (parent._privateMap != null)
                            return parent._privateMap;

                        walked.add(parent);
                    }
                }
            }
        }

        return _sharedMap;
    }

    @SuppressWarnings("unchecked")
    private final void runWritesOn(HashMap<E, Object> map) {
        if (_cleared)
            map.clear();

        if (_writes != null) {
            Iterator<Entry<E, Object>> it = _writes.entrySet().iterator();

            while (it.hasNext()) {
                Entry<E, Object> entry = it.next();

                if (entry.getValue() == Removal.Instance)
                    map.remove(entry.getKey());
                else
                    map.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void markTransactionIfModifiesTopology() {
        // If adding shares to a site or a group, remember that topology will
        // have to be recomputed

        if (getSharedObject().getId() < 0)
            getTransaction().setModifiesTopology();
    }

    //

    @SuppressWarnings("unchecked")
    @Override
    protected final boolean invalidates(TransactedObject.Version objectVersion) {
        TransactedSetVersion<E> version = (TransactedSetVersion<E>) objectVersion;

        // If set has been fully read by other transaction

        if (version._fullyRead) {
            // If it has been written by our transaction

            if (_writes != null || _cleared)
                return true;
        }

        // If only specific keys have been read by other transaction

        if (version._reads != null) {
            // If our transaction has cleared the set

            if (_cleared)
                return true;

            // If read keys have been written by our transaction

            if (_writes != null)
                for (E key : _writes.keySet())
                    if (version._reads.contains(key))
                        return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    protected void replaceSharedMapBy(HashMap<E, Object> map) {
        ((TransactedSet<E>) getSharedObject())._map = map;
    }

    @Override
    protected final void commit() {
        if (_writes != null) {
            if (_privateMap != null) {
                _sharedMap = _privateMap;
                replaceSharedMapBy(_privateMap);
            } else if (_writes != _sharedMap)
                runWritesOn(_sharedMap);

            // TODO collect on remove if no more shared
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void walkReferences(ReferencesWalker walker, boolean privateOnly, boolean trackRemovals) {
        if (privateOnly) {
            if (_writes != null)
                for (Map.Entry<E, Object> entry : _writes.entrySet())
                    onPrivateReference(walker, entry, trackRemovals);
        } else {
            for (Map.Entry<E, Object> entry : entrySet())
                walkReferences(walker, entry);
        }
    }

    private void walkReferences(ReferencesWalker walker, Map.Entry<E, Object> entry) {
        if (entry.getKey() instanceof TransactedObject)
            walker.onReference((TransactedObject) entry.getKey(), false, new EntryReference<E, Object>(entry));

        if (entry.getValue() instanceof TransactedObject)
            walker.onReference((TransactedObject) entry.getValue(), false, new EntryReference<E, Object>(entry));
    }

    private void onPrivateReference(ReferencesWalker walker, Map.Entry<E, Object> entry, boolean trackRemovals) {
        if (trackRemovals) {
            if (entry.getValue() == TransactedObject.Removal.Instance)
                if (entry.getKey() instanceof TransactedObject)
                    walker.onReference((TransactedObject) entry.getKey(), true, new EntryReference<E, Object>(entry));

            Object previous = getPrevious(walker.getTransaction(), entry.getKey());

            if (previous instanceof TransactedObject)
                walker.onReference((TransactedObject) previous, true, new EntryReference<E, Object>(entry));
        }

        if (entry.getValue() != TransactedObject.Removal.Instance) {
            walkReferences(walker, entry);
        }
    }

    protected final class EntryReference<K, V> extends Reference {

        private final Map.Entry<K, V> _entry;

        public EntryReference(Map.Entry<K, V> entry) {
            _entry = entry;
        }

        private TransactedObject getObject() {
            return TransactedSetVersion.this.getSharedObject();
        }

        @Override
        public boolean canDelete() {
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void delete() {
            if (getObject() instanceof TransactedSet) {
                TransactedSet set = (TransactedSet) getSharedObject();
                set.remove(_entry.getKey());
            }

            if (getObject() instanceof TransactedMap) {
                TransactedMap map = (TransactedMap) getSharedObject();
                map.remove(_entry.getKey());
            }
        }

        @Override
        public int hashCode() {
            return getObject().hashCode() ^ _entry.getKey().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EntryReference) {
                EntryReference other = (EntryReference) obj;

                if (other.getObject() == getObject() && other._entry.getKey().equals(_entry.getKey()))
                    return true;
            }

            return false;
        }

        @Override
        public String toString() {
            if (getObject() instanceof TransactedMap)
                return _entry.getKey().toString();

            return null;
        }
    }

    protected final void raiseSetEvents(TransactedSet.Listener<E> listener) {
        if (_writes != null) {
            for (Map.Entry<E, Object> entry : _writes.entrySet()) {
                if (entry.getValue() != Removal.Instance)
                    listener.onAdded(getTransaction(), entry.getKey());
                else
                    listener.onRemoved(getTransaction(), entry.getKey());
            }
        }
    }

    protected final void serialize(Writer writer) throws IOException {
        writer.writeBoolean(_fullyRead);
        writer.writeBoolean(_cleared);

        if (_reads != null)
            for (Object read : _reads)
                Serializer.writeObject(writer, read);

        if (_writes != null) {
            writer.writeByte(Serializer.SWITCH_TO_WRITES);

            for (Map.Entry<E, Object> entry : _writes.entrySet())
                Serializer.writeEntry(entry, writer);
        }

        writer.writeByte(Serializer.END);
    }

    @SuppressWarnings("unchecked")
    protected final void deserialize(Reader reader) throws IOException {
        _fullyRead = reader.readBoolean();
        _cleared = reader.readBoolean();

        if (_cleared)
            markTransactionIfModifiesTopology();

        byte code;

        while (true) {
            code = reader.readByte();

            if (code == Serializer.END)
                return;

            if (code == Serializer.SWITCH_TO_WRITES)
                break;

            if (_reads == null)
                _reads = new HashSet<E>();

            Object read = Serializer.readObject(code, reader);

            if (read != null || code != Serializer.TRANSACTED_OBJECT)
                _reads.add((E) read);
        }

        while (true) {
            code = reader.readByte();

            if (code == Serializer.END)
                return;

            E key = (E) Serializer.readObject(code, reader);
            code = reader.readByte();
            Object value = Serializer.readObject(code, reader);

            getOrCreateWrites().put(key, value);
        }
    }
}
