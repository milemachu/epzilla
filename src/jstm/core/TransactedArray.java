/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;

public final class TransactedArray<E> extends TransactedRandomAccess<Object> {

    public TransactedArray(int length) {
        super(length);
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) getField(index);
    }

    public void set(int index, E value) {
        setField(index, value);
    }

    public int length() {
        return _fields.length;
    }

    //

    @Override
    protected int getClassId() {
        return ObjectModel.TRANSACTED_ARRAY_CLASS_ID;
    }

    @Override
    protected jstm.core.TransactedObject.Version createVersion(Transaction transaction) {
        return new TransactedRandomAccess.Version(this);
    }

    @Override
    protected jstm.core.TransactedObject.Version getSnapshot(Transaction transaction) {
        TransactedRandomAccess.Version version = new TransactedRandomAccess.Version(this);
        version.setWrites(_fields);
        return version;
    }

    @Override
    protected void serialize(TransactedObject.Version baseVersion, Writer writer) throws IOException {
        TransactedRandomAccess.Version version = (TransactedRandomAccess.Version) baseVersion;
        version.serializeReadsAndWrites(writer);
    }

    @Override
    protected void deserialize(TransactedObject.Version baseVersion, Reader reader) throws IOException {
        TransactedRandomAccess.Version version = (TransactedRandomAccess.Version) baseVersion;
        version.deserializeReadsAndWrites(reader);
    }
}
