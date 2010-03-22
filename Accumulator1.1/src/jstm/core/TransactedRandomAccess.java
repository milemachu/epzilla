/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.RandomAccess;

import jstm.misc.Debug;

public abstract class TransactedRandomAccess<T> extends TransactedObject implements RandomAccess {

    protected T[] _fields;

    @SuppressWarnings("unchecked")
    protected TransactedRandomAccess(int length) {
        _fields = (T[]) new Object[length];
    }

    @SuppressWarnings("unchecked")
    protected final T getField(int index) {
        Transaction transaction = Transaction.getCurrent();

        if (transaction != null) {
            Version version = (Version) transaction.getVersion(this);

            if (version != null)
                return (T) version.get(transaction, index);
        }

        return _fields[index];
    }

    protected final void setField(int index, T value) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        Version version = (Version) transaction.getVersion(this);
        version.set(transaction, index, value);

        if (created)
            transaction.beginCommit(null);
    }

    // Listener

    public final void addListener(FieldListener listener) {
        addListener(listener, listener);
    }

    protected final void addListener(Object key, FieldListener listener) {
        Site.getLocal().addSiteListener(key, new SiteListener(listener));
    }

    public final void removeListener(FieldListener listener) {
        removeListener((Object) listener);
    }

    protected final void removeListener(Object key) {
        Site.getLocal().removeTransactionListener(key);
    }

    private final class SiteListener implements Site.Listener {

        public final FieldListener Listener;

        public SiteListener(FieldListener listener) {
            Listener = listener;
        }

        public void onCommitted(Transaction transaction) {
            if (transaction.getPrivateObjects() != null) {
                Version version = (Version) transaction.getPrivateObjects().get(TransactedRandomAccess.this);

                if (version != null)
                    version.raiseEvents(transaction, Listener);
            }
        }

        public void onAborted(Transaction transaction) {
        }
    }

    //

    public static class Version extends TransactedObject.Version {

        private boolean[] _reads;

        private Object[] _writes;

        public Version(TransactedRandomAccess shared) {
            super(shared);
        }

        protected final TransactedRandomAccess getRandomAccess() {
            return (TransactedRandomAccess) getSharedObject();
        }

        public final boolean[] getReads() {
            return _reads;
        }

        public final void setReads(boolean[] value) {
            _reads = value;
        }

        public final Object[] getWrites() {
            return _writes;
        }

        public final void setWrites(Object[] value) {
            _writes = value;
        }

        protected int size() {
            return getRandomAccess()._fields.length;
        }

        private final int capacity() {
            if (_reads != null)
                return _reads.length;

            if (_writes != null)
                return _writes.length;

            return size();
        }

        public final Object get(Transaction transaction, int index) {
            Object override = getOverride(transaction, index);

            if (override != null)
                return override == Removal.Instance ? null : override;

            return getRandomAccess()._fields[index];
        }

        public final Object getPrevious(Transaction transaction, int index) {
            Object override = getPreviousOverride(transaction, index);

            if (override != null)
                return override == Removal.Instance ? null : override;

            return getRandomAccess()._fields[index];
        }

        private final Object getOverride(Transaction transaction, int index) {
            if (_writes != null && _writes[index] != null)
                return _writes[index];

            // Otherwise use current value and keep track of read

            if (transaction.getStatus() == Transaction.Status.RUNNING) {
                if (_reads == null) {
                    _reads = new boolean[capacity()];
                    assertLengths();
                }

                _reads[index] = true;
            }

            return getPreviousOverride(transaction, index);
        }

        private final Object getPreviousOverride(Transaction transaction, int index) {
            // Each version walks the entire dependence list, no recursivity

            if (transaction.getPotentialDependencies() != null) {
                for (Transaction dependency : transaction.getPotentialDependencies()) {
                    if (dependency.getPrivateObjects() != null) {
                        Version parent = (Version) dependency.getPrivateObjects().get(getRandomAccess());

                        if (parent != null) {
                            Object override = null;

                            if (parent._writes != null)
                                override = parent._writes[index];

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

        public final void set(Transaction transaction, int index, Object value) {
            if (_writes == null) {
                _writes = new Object[capacity()];
                assertLengths();
            }

            if (value != null)
                _writes[index] = value;
            else
                _writes[index] = Removal.Instance;

            if (transaction.hasListeners())
                transaction.raiseNewChange(new Change.Field(transaction, this, index));
        }

        @Override
        protected void commit() {
            if (getWrites() != null) {
                Object[] fields = getRandomAccess()._fields;

                if (getWrites() != fields) {
                    for (int i = 0; i < getWrites().length; i++) {
                        if (getWrites()[i] != null) {
                            if (getWrites()[i] != Removal.Instance)
                                fields[i] = getWrites()[i];
                            else if (i < fields.length)
                                fields[i] = null;
                        }
                    }
                }
            }
        }

        @Override
        protected boolean invalidates(TransactedObject.Version objectVersion) {
            Version version = (Version) objectVersion;
            boolean[] reads = version.getReads();

            // If fields have been read by given transaction

            if (reads != null) {
                if (getWrites() != null) {
                    Debug.assertion(reads.length == _writes.length);

                    // Search common fields

                    for (int i = 0; i < reads.length; i++)
                        if (reads[i] && _writes[i] != null)
                            return true;
                }
            }

            return false;
        }

        @Override
        public void walkReferences(ReferencesWalker walker, boolean privateOnly, boolean trackRemovals) {
            if (getWrites() != null) {
                for (int i = 0; i < getWrites().length; i++) {
                    Object o = getWrites()[i];

                    if (o == TransactedObject.Removal.Instance) {
                        if (trackRemovals) {
                            Object previous = getPrevious(walker.getTransaction(), i);

                            if (previous instanceof TransactedObject)
                                walker.onReference((TransactedObject) previous, true, new FieldReference(this, i));
                        }
                    } else if (o instanceof TransactedObject) {
                        walker.onReference((TransactedObject) o, false, new FieldReference(this, i));
                    }
                }
            }

            if (!privateOnly) {
                for (int i = 0; i < size(); i++) {
                    Object o = get(walker.getTransaction(), i);

                    if (o instanceof TransactedObject)
                        walker.onReference((TransactedObject) o, false, new FieldReference(this, i));
                }
            }
        }

        protected static final class FieldReference extends Reference {

            private final TransactedRandomAccess.Version _version;

            private final int _index;

            public FieldReference(TransactedRandomAccess.Version version, int index) {
                _version = version;
                _index = index;
            }

            private TransactedRandomAccess getObject() {
                return (TransactedRandomAccess) _version.getSharedObject();
            }

            @Override
            public boolean canDelete() {
                return true;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void delete() {
                if (getObject() instanceof TransactedList) {
                    TransactedList list = (TransactedList) getObject();
                    list.removeFast(_index);
                } else {
                    getObject().setField(_index, null);
                }
            }

            @Override
            public int hashCode() {
                return getObject().hashCode() ^ _index;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof FieldReference) {
                    FieldReference other = (FieldReference) obj;

                    if (other.getObject() == getObject() && other._index == _index)
                        return true;
                }

                return false;
            }

            @Override
            public String toString() {
                return _version.getFieldName(_index);
            }
        }

        // TODO serialize using derivative of value & type cache

        public final void serializeReadsAndWrites(Writer writer) throws IOException {
            if (_reads != null) {
                writer.writeInteger(Integer.MAX_VALUE);

                for (int i = 0; i < size(); i++)
                    writer.writeBoolean(_reads[i]);
            }

            if (_writes != null) {
                for (int i = 0; i < size(); i++) {
                    if (_writes[i] != null) {
                        if (_writes[i] == TransactedObject.Removal.Instance)
                            writer.writeInteger(-i - 1);
                        else {
                            writer.writeInteger(i + 1);

                            Serializer.writeObject(writer, _writes[i]);
                        }
                    }
                }
            }

            writer.writeInteger(0);
        }

        public final void deserializeReadsAndWrites(Reader reader) throws IOException {
            int index = reader.readInteger();

            if (index == Integer.MAX_VALUE) {
                _reads = new boolean[size()];

                for (int i = 0; i < size(); i++)
                    _reads[i] = reader.readBoolean();

                index = reader.readInteger();
            }

            while (index != 0) {
                if (index > 0) {
                    if (_writes == null)
                        _writes = new Object[size()];

                    byte code = reader.readByte();
                    _writes[index - 1] = Serializer.readObject(code, reader);
                    index = reader.readInteger();
                } else {
                    if (_writes == null)
                        _writes = new Object[size()];

                    _writes[-index - 1] = TransactedObject.Removal.Instance;
                    index = reader.readInteger();
                }
            }
        }

        protected final void raiseEvents(Transaction transaction, FieldListener listener) {
            if (_writes!=null) {
				for (int i = 0; i < _writes.length; i++)
					if (_writes[i] != null)
						listener.onChange(transaction, i);
			}
        }

        protected final void assertLengths() {
            if (_reads != null && _writes != null)
                Debug.assertion(_reads.length == _writes.length);
        }

        protected String getFieldName(int index) {
            return "Field " + index;
        }

        protected String writeChange(int index) {
            String name = getFieldName(index);
            String after;

            if (getWrites()[index] == Removal.Instance)
                after = "null";
            else
                after = getWrites()[index].toString();

            return name + ": " + getRandomAccess()._fields[index] + " -> " + after;
        }

        @Override
        public final String toString() {
            StringBuilder s = new StringBuilder();

            if (getWrites() != null) {
                for (int i = 0; i < getWrites().length; i++) {
                    if (getWrites()[i] != null) {
                        if (s.length() != 0)
                            s.append(", ");

                        s.append(writeChange(i));
                    }
                }
            }

            return getRandomAccess() + ": " + s.toString();
        }
    }
}