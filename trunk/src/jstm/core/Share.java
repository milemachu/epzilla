/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jstm.misc.Debug;

public final class Share extends TransactedSet<TransactedObject> {

    /**
     * When adding an object to a group or when setting a field on an object
     * which is in a group, this specifies if the whole tree of objects
     * referenced by it must be added to the group also.
     */
    // Java enums cause pblms with gwt
    public static final class ReferencesWalkingMode {

        public static final ReferencesWalkingMode MANUAL = new ReferencesWalkingMode("Manual");

        public static final ReferencesWalkingMode ADD_NON_TRANSIENT = new ReferencesWalkingMode("AddNonTransient");

        private final String _name;

        private ReferencesWalkingMode(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    private final ArrayList<Connection> _connections = new ArrayList<Connection>();

    // TODO: transact this field
    private ReferencesWalkingMode _mode = ReferencesWalkingMode.ADD_NON_TRANSIENT;

    private boolean _opened;

    public Share() {
    }

    public ReferencesWalkingMode getReferencesWalkingMode() {
        return _mode;
    }

    public void setReferencesWalkingMode(ReferencesWalkingMode value) {
        _mode = value;
    }

    protected ArrayList<Connection> getConnections() {
        return _connections;
    }

    protected boolean opened() {
        return _opened;
    }

    protected void open() {
        _opened = true;
    }

    protected void close() {
        // TODO fix (close is called on each share at each topology change)
        // _map.clear();
        _opened = false;
    }

    public static class Version extends TransactedSetVersion<TransactedObject> {

        public Version(Share share, Transaction transaction, HashMap<TransactedObject, Object> sharedMap, boolean snapshot) {
            super(share, transaction, sharedMap, snapshot);
        }
    }

    //

    @SuppressWarnings("unchecked")
    protected void updateAndPrepareConnections(Transaction transaction) {
        TransactedSetVersion<TransactedObject> version = null;

        if (transaction.getPrivateObjects() != null)
            version = (TransactedSetVersion) transaction.getPrivateObjects().get(this);

        // Search if new objects have been indirectly added to the share by
        // new references from already shared objects

        Walker walker = null;

        if (getReferencesWalkingMode() == ReferencesWalkingMode.ADD_NON_TRANSIENT)
            walker = new Walker(transaction, version);

        if (transaction.getPrivateObjects() != null) {
            for (Map.Entry<TransactedObject, TransactedObject.Version> entry : transaction.getPrivateObjects().entrySet()) {
                if (walker != null)
                    version = walker.getVersion();

                if (containsWithoutRecord(transaction, version, entry.getKey()))
                    if (walker != null)
                        entry.getValue().walkReferences(walker, true, false);

                // If previous version was containing object, it is shared

                if (containsWithoutRecord(transaction, null, entry.getKey()))
                    addAsShared(entry.getKey(), entry.getValue());
            }
        }

        if (walker != null) {
            if (walker.getCreated())
                walker.registerVersion();

            if (walker.getVersion() != null) {
                for (Connection connection : _connections) {
                    // Add newly shared objects to connections

                    for (Map.Entry<TransactedObject, Object> entry : walker.getVersion().getWrites().entrySet())
                        if (entry.getValue() != Removal.Instance)
                            connection.addNew(entry.getKey(), transaction);

                    connection.addShared(this, walker.getVersion());
                }
            }
        }
    }

    private void addAsShared(TransactedObject o, TransactedObject.Version version) {
        for (Connection connection : _connections)
            connection.addShared(o, version);
    }

    //

    @Override
    protected int getClassId() {
        return ObjectModel.Default.SHARE_CLASS_ID;
    }

    //    

    private final class Walker implements ReferencesWalker {

        private final Transaction _transaction;

        private TransactedSetVersion<TransactedObject> _version;

        private boolean _created;

        public Walker(Transaction transaction, TransactedSetVersion<TransactedObject> version) {
            _transaction = transaction;
            _version = version;
        }

        public Transaction getTransaction() {
            return _transaction;
        }

        public TransactedSetVersion<TransactedObject> getVersion() {
            return _version;
        }

        @SuppressWarnings("unchecked")
        public void onReference(TransactedObject o, boolean removal, TransactedObject.Reference reference) {
            Debug.assertion(!removal);

            if (!Share.this.containsWithoutRecord(_transaction, _version, o)) {
                if (_version == null) {
                    _version = (TransactedSetVersion) createVersion(_transaction);
                    _created = true;
                }

                _version.add(o);

                TransactedObject.Version version = getTransaction().getPrivateObjects().get(o);

                if (version != null)
                    version.walkReferences(this, false, false);
                else
                    o.createVersion(getTransaction()).walkReferences(this, false, false);
            }
        }

        public boolean getCreated() {
            return _created;
        }

        public void registerVersion() {
            _transaction.getOrCreatePrivateObjects().put(Share.this, _version);
            _created = false;
        }
    }
}