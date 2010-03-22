/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.io.Serializable;

import jstm.misc.Debug;

/**
 * This is the base class for all transacted objects. Some methods on transacted
 * objects need to be called in the context of a transaction, like a setter on a
 * transacted structure. The value given to the setter will only be applied on
 * the structure if the transaction commits. Most of those methods will start a
 * transaction if no one was associated to the current thread. If you need to
 * update several fields on an object, performance wise it is preferable to
 * start a transaction yourself and do all the updates in its context. <br>
 * <br>
 * The behavior of some methods is not exactly the same if called in the context
 * of a transaction or they have to start their own. E.g. the add(Object) method
 * on TransactedSet returns a boolean indicating if the object was already
 * present in the set. If the method is not called in the context of a
 * transaction, it will start one to add the object and commit it
 * asynchronously. The fact that there was already an object in the set will be
 * known only once the commit is finished. This might take some time, in
 * particular if the object is shared with remote machines as the commit might
 * have to be acknowledged by a remote coordinator. In the meantime, another
 * transaction can add or remove the object from the set. It would be too slow
 * to wait for the commit to complete on each method call, so the method returns
 * immediately, without knowing if there is or not an object in the set. That is
 * why the current implementation always return false if it is not called in the
 * context of a transaction. All methods that return a value as a side effect of
 * updating an object have this behavior.
 */
public class TransactedObject {

    private Site _origin;

    private int _id = Id.NULL.Value;

    public TransactedObject() {
    }

    public final Site getOrigin() {
        return _origin;
    }

    protected final void setOrigin(Site value) {
        _origin = value;
    }

    /**
     * Negative means part of the topology -> Not removed when unshared.
     */
    protected final int getId() {
        return _id;
    }

    protected final void setId(int value) {
        Debug.assertion(_id == 0 || value == TransactedObject.Id.DISCONNECTED.Value);
        _id = value;
    }

    protected Version createVersion(Transaction transaction) {
        return new Version(this);
    }

    protected Version getSnapshot(Transaction transaction) {
        return new Version(this);
    }

    public String getObjectModelUID() {
        return ObjectModel.Default.UID;
    }

    protected static class Version {

        private final TransactedObject _sharedObject;

        protected Version(TransactedObject sharedObject) {
            if (sharedObject == null)
                throw new IllegalArgumentException();

            _sharedObject = sharedObject;
        }

        protected final TransactedObject getSharedObject() {
            return _sharedObject;
        }

        protected boolean invalidates(Version version) {
            return false;
        }

        protected void commit() {
        }

        protected void walkReferences(ReferencesWalker walker, boolean privateOnly, boolean trackRemovals) {
        }
    }

    /**
     * Means a field has been set to null or removed. A type is needed in
     * addition to the constant instance for serialization.
     */
    protected static final class Removal implements Serializable {

        public static final Removal Instance = new Removal();

        private Removal() {
        }
    }

    protected int getClassId() {
        return ObjectModel.Default.TRANSACTED_OBJECT_CLASS_ID;
    }

    protected static interface ReferencesWalker {

        Transaction getTransaction();

        void onReference(TransactedObject o, boolean removal, TransactedObject.Reference reference);
    }

    // Binary representation

    @SuppressWarnings("unused")
    protected void serialize(Version version, Writer writer) throws IOException {
    }

    @SuppressWarnings("unused")
    protected void deserialize(Version version, Reader reader) throws IOException {
    }

    //

    protected static final class Id implements Serializable, Comparable<Id> {

        protected static final Id NULL = new Id(0);

        protected static final Id SNAPSHOTS = new Id(Integer.MIN_VALUE);

        protected static final Id VERSIONS = new Id(Integer.MIN_VALUE + 1);

        protected static final Id CALLS = new Id(Integer.MIN_VALUE + 2);

        protected static final Id TAGS = new Id(Integer.MIN_VALUE + 3);

        protected static final Id SITES = new Id(Integer.MIN_VALUE + 4);

        protected static final Id NULL_SITE = new Id(Integer.MIN_VALUE + 5);

        protected static final Id DISCONNECTED = new Id(Integer.MIN_VALUE + 6);

        protected static final Id SITE = new Id(Integer.MIN_VALUE + 7);

        protected static final Id END = new Id(Integer.MIN_VALUE + 8);

        public int Value;

        public Id(int value) {
            Value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Id)
                return Value == (((Id) obj).Value);

            return false;
        }

        @Override
        public int hashCode() {
            return Value;
        }

        public int compareTo(Id other) {
            if (Value == other.Value)
                return 0;

            return Value < other.Value ? -1 : 1;
        }

        @Override
        public String toString() {
            return "Id(" + Value + ")";
        }
    }

    public static abstract class Reference {

        public abstract boolean canDelete();

        public abstract void delete();
    }
}