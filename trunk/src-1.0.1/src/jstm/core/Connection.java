/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.HashMap;

import jstm.misc.Debug;
import jstm.misc.OverrideAssert;

/**
 * Connection to other sites or systems. This class is JSTM's main extension
 * point. Network transports and persistence can be implemented as particular
 * connections.
 */
public abstract class Connection {

    private final Site _local;

    private final HashMap<TransactedObject, TransactedObject.Version> _new = new HashMap<TransactedObject, TransactedObject.Version>();

    private final HashMap<TransactedObject, TransactedObject.Version> _shared = new HashMap<TransactedObject, TransactedObject.Version>();

    private boolean _registered;

    private Group _targetGroup;

    private boolean _hasCalls;

    protected Connection(Site local) {
        _local = local;
    }

    protected final void registerConnection(Group targetGroup) {
        synchronized (getManager().getLock()) {
            if (_registered)
                throw new IllegalStateException("Already registered");

            Debug.assertion(!getManager().getTopology().getConnections().contains(this));
            getManager().getTopology().getConnections().add(this);

            _targetGroup = targetGroup;

            if (_targetGroup != null)
                getManager().getTopology().getGroups().add(_targetGroup);

            getManager().getTopology().markAsInvalid();

            _registered = true;
        }
    }

    protected final void dispose(IOException ex) {
        OverrideAssert.add(this);
        _dispose(ex);
        OverrideAssert.end(this);
    }

    protected void _dispose(IOException ex) {
        OverrideAssert.set(this);

        synchronized (getManager().getLock()) {
            if (_registered) {
                Debug.assertion(getManager().getTopology().getConnections().contains(this));
                getManager().getTopology().getConnections().remove(this);

                if (_targetGroup != null)
                    getManager().getTopology().getGroups().remove(_targetGroup);

                getManager().getTopology().markAsInvalid();

                _registered = false;
            }
        }

        getManager().pruneWriteTrail();
    }

    protected final Group getTargetGroup() {
        return _targetGroup;
    }

    //

    protected abstract boolean interceptsCommits();

    protected abstract boolean interceptsCommitsCanChange();

    protected abstract void onInterception(CommitInterception interception);

    protected abstract void onCommit(Transaction transaction, long newVersion);

    protected abstract void onAbort(Transaction transaction);

    protected abstract Long getAcknowledgedVersion();

    //

    protected final Site getLocalSite() {
        return _local;
    }

    protected final TransactionManager getManager() {
        return _local.getManager();
    }

    protected final HashMap<TransactedObject, TransactedObject.Version> getNew() {
        return _new;
    }

    protected final HashMap<TransactedObject, TransactedObject.Version> getShared() {
        return _shared;
    }

    protected final boolean containsNew(TransactedObject o) {
        return _new.containsKey(o);
    }

    protected final void addNewWithVersion(TransactedObject o, TransactedObject.Version version) {
        if (o.getId() == 0)
            _local.getManager().register(o);

        if (o.getId() > 0)
            _new.put(o, version);
    }

    protected final void addNew(TransactedObject o, Transaction transaction) {
        if (o.getId() == 0)
            _local.getManager().register(o);

        if (o.getId() > 0) {
            TransactedObject.Version version = transaction.getOrCreatePrivateObjects().get(o);
            _new.put(o, version);
        }
    }

    protected final void addShared(TransactedObject o, TransactedObject.Version version) {
        Debug.assertion(o != null && version != null);

        if (o.getId() == 0)
            _local.getManager().register(o);

        if (o.getId() > 0)
            _new.remove(o);
        else
            Debug.assertion(!_new.containsKey(o));

        _shared.put(o, version);
    }

    protected final Connection getRoute(Transaction transaction) {
        return transaction.getOrigin().getRoute();
    }

    protected final boolean involved() {
        return getNew().size() > 0 || getShared().size() > 0 || _hasCalls;
    }

    protected final void notifyHasCalls() {
        _hasCalls = true;
    }

    protected final void reset() {
        getNew().clear();
        getShared().clear();
        _hasCalls = false;
    }

    protected final Group createGroup() {
        ObjectModel model = getLocalSite().getDefaultObjectModel();
        int classId = ObjectModel.Default.GROUP_CLASS_ID;
        Group group = (Group) model.createInstance(classId, this);
        group.createOpenShares();

        synchronized (getManager().getLock()) {
            getManager().registerAsTopologyInvolved(group);
            getManager().registerAsTopologyInvolved(group.getOpenShares());
        }

        return group;
    }

    protected final void allowThread(Site site) {
        if (Debug.Level > 0) {
            TransactionManager manager = site.getManager();
            manager.allowThread();
        }
    }
}
