/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;

public abstract class TransactedStructure extends TransactedRandomAccess<Object> {

    protected TransactedStructure(int length) {
        super(length);
    }

    public final int getFieldCount() {
        return _fields.length;
    }

    public abstract String getFieldName(int index);

    protected abstract int[] getNonTransientFields();

    public final Object get(int index) {
        return getField(index);
    }

    public final void set(int index, Object value) {
        setField(index, value);
    }

    protected final void addCall(int index, Object[] args, MethodCallback callback) {
        Transaction transaction = Transaction.getCurrent();
        boolean created = transaction == null;
        transaction = Transaction.startIfNeeded(transaction);

        MethodCall call = new MethodCall(this, index, args);
        call._callback = callback;
        // Make a child transaction to current
        // It will be coordinated by local site
        transaction.getOrCreateCalls().add(call);

        if (created)
            transaction.beginCommit(null);
    }

    protected Object call(int index, Object[] args) {
        throw new IllegalStateException();
    }

    @SuppressWarnings("unused")
    protected void serializeArguments(int index, Object[] args, Writer writer) throws IOException {
        throw new IllegalStateException();
    }

    @SuppressWarnings("unused")
    protected Object[] deserializeArguments(int index, Reader reader) throws IOException {
        throw new IllegalStateException();
    }

    @SuppressWarnings("unused")
    protected void serializeResult(int index, Object value, Writer writer) throws IOException {
        throw new IllegalStateException();
    }

    @SuppressWarnings("unused")
    protected Object deserializeResult(int index, Reader reader) throws IOException {
        throw new IllegalStateException();
    }

    @Override
    protected final TransactedObject.Version createVersion(Transaction transaction) {
        return new Version(this);
    }

    @Override
    protected final TransactedObject.Version getSnapshot(Transaction transaction) {
        Version version = (Version) createVersion(transaction);
        version.setWrites(_fields);
        return version;
    }

    public static final class Version extends TransactedRandomAccess.Version {

        public Version(TransactedStructure structure) {
            super(structure);
        }

        private TransactedStructure getStructure() {
            return (TransactedStructure) getSharedObject();
        }

        @Override
        public void walkReferences(ReferencesWalker walker, boolean privateOnly, boolean trackRemovals) {
            if (getWrites() != null)
                for (int i : getStructure().getNonTransientFields())
                    if (getWrites()[i] != null)
                        onReference(walker, i, trackRemovals);

            if (!privateOnly) {
                for (int i : getStructure().getNonTransientFields()) {
                    TransactedObject o = (TransactedObject) getStructure().get(i);

                    if (o != null)
                        walker.onReference(o, false, new FieldReference(this, i));
                }
            }
        }

        private void onReference(ReferencesWalker walker, int index, boolean trackRemovals) {
            if (trackRemovals) {
                Object previous = getPrevious(walker.getTransaction(), index);

                if (previous != null)
                    walker.onReference((TransactedObject) previous, true, new FieldReference(this, index));
            }

            if (getWrites()[index] != Removal.Instance)
                walker.onReference((TransactedObject) getWrites()[index], false, new FieldReference(this, index));
        }

        @Override
        protected String getFieldName(int index) {
            return getStructure().getFieldName(index);
        }
    }
}
