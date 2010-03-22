/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.AbstractList;
import java.util.RandomAccess;

import jstm.misc.Debug;
import jstm.misc.Utils;

final class TransactedListVersion<E> extends TransactedRandomAccess.Version {

    private final Transaction _transaction;

    private VersionList<E> _privateList;

    private int _initialSize;

    private boolean _abortIfSizeChanged;

    public TransactedListVersion(Transaction transaction, TransactedList<E> list, boolean snapshot) {
        super(list);

        if (transaction == null || list == null)
            throw new IllegalArgumentException();

        _transaction = transaction;

        if (snapshot) {
            getPrivateList()._size = list._size;
            setWrites(list._fields);
        } else {
            int size = list._size;

            if (_transaction.getPotentialDependencies() != null) {
                for (Transaction dependency : _transaction.getPotentialDependencies()) {
                    if (dependency.getPrivateObjects() != null) {
                        TransactedListVersion previous = (TransactedListVersion) dependency.getPrivateObjects().get(list);

                        if (previous != null && previous.size() != size) {
                            _transaction.confirmDependency(dependency);
                            size = previous.size();
                            break;
                        }
                    }
                }
            }

            _initialSize = size;
        }
    }

    @SuppressWarnings("unchecked")
    public TransactedList<E> getTransactedList() {
        return (TransactedList<E>) getSharedObject();
    }

    public Transaction getTransaction() {
        return _transaction;
    }

    public VersionList<E> getPrivateList() {
        if (_privateList == null)
            _privateList = new VersionList<E>(this);

        return _privateList;
    }

    public void abortIfSizeChanged() {
        _abortIfSizeChanged = true;
    }

    public int getInitialSize() {
        return _initialSize;
    }

    @Override
    protected int size() {
        if (_privateList != null)
            return _privateList.size();

        return _initialSize;
    }

    @Override
    protected boolean invalidates(TransactedObject.Version baseVersion) {
        TransactedListVersion version = (TransactedListVersion) baseVersion;

        if (version._abortIfSizeChanged) {
            if (size() != _initialSize)
                return true;
        }

        // If fields have been read by given transaction, it will be invalidated
        // if we shortened the list of wrote to same fields

        boolean[] reads = version.getReads();

        if (reads != null) {
            for (int i = 0; i < version.size(); i++) {
                if (reads[i]) {
                    if (i >= size())
                        return true;

                    if (getWrites() != null && getWrites()[i] != null)
                        return true;
                }
            }
        }

        // If fields have been written by given transaction, it will be
        // invalidated if we shortened the list

        Object[] writes = version.getWrites();

        if (writes != null) {
            for (int i = size(); i < version.size(); i++) {
                if (writes[i] != null)
                    return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void commit() {
        if (_privateList != null)
            _privateList.onCommit();

        Debug.assertion(getTransactedList()._size == _initialSize);
        getTransactedList().resize(size());

        super.commit();
    }

    @SuppressWarnings("unchecked")
    protected void raiseSizeEvents(SizeListener listener) {
        if (size() != _initialSize)
            listener.onResize(getTransaction(), _initialSize, size());
    }

    // TODO encode special value meaning to take next value in shared list. (to
    // help replicate removals)

    public void serialize(Writer writer) throws IOException {
        writer.writeBoolean(_abortIfSizeChanged);
        writer.writeInteger(_initialSize);
        writer.writeInteger(size());

        boolean[] reads = getReads();
        Object[] writes = getWrites();

        if (reads != null) {
            writer.writeInteger(Integer.MAX_VALUE);

            for (int i = 0; i < size(); i++)
                writer.writeBoolean(reads[i]);
        }

        if (writes != null) {
            for (int i = 0; i < size(); i++) {
                if (writes[i] != null) {
                    if (writes[i] == TransactedObject.Removal.Instance)
                        writer.writeInteger(-i - 1);
                    else {
                        writer.writeInteger(i + 1);

                        Serializer.writeObject(writer, writes[i]);
                    }
                }
            }
        }

        writer.writeInteger(0);
    }

    public void deserialize(Reader reader) throws IOException {
        boolean[] reads = null;
        Object[] writes = null;

        _abortIfSizeChanged = reader.readBoolean();
        _initialSize = reader.readInteger();

        int size = reader.readInteger();

        if (size != _initialSize)
            getPrivateList()._size = size;

        int index = reader.readInteger();

        if (index == Integer.MAX_VALUE) {
            reads = new boolean[size()];

            for (int i = 0; i < size(); i++)
                reads[i] = reader.readBoolean();

            index = reader.readInteger();
        }

        for (int i = 0; index != 0 && i < size(); i++) {
            if (index == i + 1) {
                if (writes == null)
                    writes = new Object[size()];

                byte code = reader.readByte();
                writes[i] = Serializer.readObject(code, reader);
                index = reader.readInteger();
            } else if (index == -i - 1) {
                if (writes == null)
                    writes = new Object[size()];

                writes[i] = TransactedObject.Removal.Instance;
                index = reader.readInteger();
            }
        }

        setReads(reads);
        setWrites(writes);
    }

    @SuppressWarnings("unchecked")
    public static final class VersionList<T> extends AbstractList<T> implements RandomAccess {

        private final TransactedListVersion _version;

        protected int _size;

        public VersionList(TransactedListVersion version) {
            _version = version;
            _size = version._initialSize;
        }

        public TransactedListVersion getListVersion() {
            return _version;
        }

        @Override
        public T get(int index) {
            if (index >= _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            return getUnchecked(index);
        }

        private T getUnchecked(int index) {
            return (T) _version.get(_version.getTransaction(), index);
        }

        @Override
        public T set(int index, T value) {
            if (index >= _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            T previous = (T) _version.get(_version.getTransaction(), index);
            _version.set(_version.getTransaction(), index, value);
            return previous;
        }

        public void setFast(int index, T value) {
            if (index >= _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            _version.set(_version.getTransaction(), index, value);
        }

        @Override
        public void add(int index, T element) {
            if (index > _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            modCount++;
            setSize(_size + 1);

            ensureCapacity();

            for (int i = _size - 2; i >= index; i--)
                _version.set(_version.getTransaction(), i + 1, getUnchecked(i));

            _version.set(_version.getTransaction(), index, element);

        }

        @Override
        public T remove(int index) {
            T element = getUnchecked(index);
            removeFast(index);
            return element;
        }

        public void removeFast(int index) {
            if (index >= _size || index < 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _size);

            modCount++;

            for (int i = index; i < _size - 1; i++)
                _version.set(_version.getTransaction(), i, getUnchecked(i + 1));

            setSize(_size - 1);
        }

        @Override
        public int size() {
            return _size;
        }

        private void setSize(int value) {
            if (value == 0) {
                _version.setWrites(null);
            } else if (value < _size && _version.getWrites() != null) {
                for (int i = value; i < _size; i++) {
                    _version.getWrites()[i] = null;
                }
            }

            _size = value;

            Transaction transaction = _version.getTransaction();

            if (transaction.hasListeners())
                transaction.raiseNewChange(new Change.Size(transaction, this));
        }

        @Override
        public void clear() {
            modCount++;
            setSize(0);
        }

        protected void onCommit() {
            modCount++;
        }

        private void ensureCapacity() {
            boolean[] reads = _version.getReads();
            Object[] writes = _version.getWrites();

            if (reads != null || writes != null) {
                int oldCapacity = writes != null ? writes.length : 0;

                if (size() > oldCapacity) {
                    int newCapacity = (oldCapacity * 3) / 2 + 1;

                    if (newCapacity < size())
                        newCapacity = size();

                    if (reads != null) {
                        _version.setReads(new boolean[newCapacity]);
                        Utils.copy(reads, 0, _version.getReads(), 0, size() - 1);
                    }

                    if (writes != null) {
                        _version.setWrites(new Object[newCapacity]);
                        Utils.copy(writes, 0, _version.getWrites(), 0, size() - 1);
                    }

                    _version.assertLengths();
                }
            }
        }
    }
}