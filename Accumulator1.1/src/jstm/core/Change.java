/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import jstm.core.TransactedObject.Removal;
import jstm.core.TransactedObject.Version;

/**
 * Represents a change made to an object by a transaction. This class is not
 * used for most scenarios. It is involved only if you want to explore or
 * manipulate the history of a transaction. E.g. if you register a listener on a
 * transaction, instances of this class will be created and given to your
 * listener each time an object is modified. When a transaction has committed or
 * aborted, it is not possible to modify its changes anymore.
 */
public abstract class Change {

    private final Transaction _transaction;

    protected Change(Transaction transaction) {
        _transaction = transaction;
    }

    public Transaction getTransaction() {
        return _transaction;
    }

    public abstract TransactedObject getObject();

    protected abstract TransactedObject.Version getDelta();

    public final void cancel() {
        if (!getTransaction().isActive()) {
            String message = "The transaction which made this change has committed or ";
            message += "aborted. This change cannot be cancelled anymore.";
            throw new RuntimeException(message);
        }
    }

    protected abstract void _cancel();

    public boolean replaces(Change change) {
        return false;
    }

    protected static final class Field extends Change {

        private final TransactedRandomAccess.Version _version;

        private final int _index;

        protected Field(Transaction transaction, TransactedRandomAccess.Version version, int index) {
            super(transaction);

            _version = version;
            _index = index;
        }

        @Override
        public TransactedObject getObject() {
            return _version.getSharedObject();
        }

        @Override
        protected Version getDelta() {
            return _version;
        }

        @Override
        protected void _cancel() {
            _version.getWrites()[_index] = null;
        }

        @Override
        public boolean replaces(Change change) {
            if (change instanceof Field) {
                Field other = (Field) change;

                if (other._version == _version)
                    if (other._index == _index)
                        return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return _version.writeChange(_index);
        }
    }

    protected static final class Size extends Change {

        private final TransactedListVersion.VersionList _version;

        protected Size(Transaction transaction, TransactedListVersion.VersionList version) {
            super(transaction);

            _version = version;
        }

        @Override
        public TransactedObject getObject() {
            return _version.getListVersion().getSharedObject();
        }

        @Override
        protected Version getDelta() {
            return _version.getListVersion();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void _cancel() {
            _version._size = _version.getListVersion().getInitialSize();
        }

        @Override
        public boolean replaces(Change change) {
            if (change instanceof Size) {
                Size other = (Size) change;

                if (other._version == _version)
                    return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return "Size " + _version.getListVersion().getInitialSize() + " -> " + _version._size;
        }
    }

    protected static final class Clear extends Change {

        private final TransactedSetVersion _version;

        protected Clear(Transaction transaction, TransactedSetVersion version) {
            super(transaction);

            _version = version;
        }

        @Override
        public TransactedObject getObject() {
            return _version.getSharedObject();
        }

        @Override
        protected Version getDelta() {
            return _version;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void _cancel() {
            _version.cancelClear();
        }

        @Override
        public boolean replaces(Change change) {
            if (change instanceof Clear) {
                Clear other = (Clear) change;

                if (other._version == _version)
                    return true;
            }

            if (change instanceof Set) {
                Set other = (Set) change;

                if (other._version == _version)
                    return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return "Clear";
        }
    }

    protected static final class Set extends Change {

        private final TransactedSetVersion _version;

        private final Object _key;

        protected Set(Transaction transaction, TransactedSetVersion version, Object key) {
            super(transaction);

            _version = version;
            _key = key;
        }

        @Override
        public TransactedObject getObject() {
            return _version.getSharedObject();
        }

        @Override
        protected Version getDelta() {
            return _version;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void _cancel() {
            _version.getWrites().remove(_key);
        }

        @Override
        public boolean replaces(Change change) {
            if (change instanceof Set) {
                Set other = (Set) change;

                if (other._version == _version)
                    if (_key.equals(other._key))
                        return true;
            }

            return false;
        }

        @Override
        public String toString() {
            Object write = _version.getWrites().get(_key);
            Object previous = _version.getSharedMap().get(_key);

            if (write == Removal.Instance)
                return _key + ": " + previous + " -> null";

            return _key + ": " + previous + " -> " + write;
        }
    }
}
