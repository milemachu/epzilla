/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import jstm.core.Transaction.Status;
import jstm.misc.Debug;
import jstm.misc.Queue;
import jstm.misc.Utils;

final class TransactionManager {

    protected static final int MAX_EXPECTED_WRITE_TRAIL_LENGTH = 1000;

    private final Site _local;

    private final HashSet<Transaction> _transactions = new HashSet<Transaction>();

    private final WriteTrail _writeTrail;

    private final Topology _topology;

    private final Queue<Transaction> _intercepted = new Queue<Transaction>();

    private final ArrayList<Site.InternalListener> _internalListeners = new ArrayList<Site.InternalListener>();

    private final ConcurrentHashMap<Object, Site.Listener> _siteListeners = new ConcurrentHashMap<Object, Site.Listener>();

    private final ArrayList<TransactedObject.Id> _freeObjectIds = new ArrayList<TransactedObject.Id>();

    private final HashMap<String, ObjectModel> _knownObjectModels = new HashMap<String, ObjectModel>();

    private HashSet<Thread> _allowedThreads;

    // Start at 1 for client shares (Server version of 0 means not synchronized)
    private long _versionNumber = 1;

    private long _nextTransactionId;

    private int _nextObjectId;

    public TransactionManager(Site local) {
        _local = local;

        _writeTrail = new WriteTrail(this);
        _topology = new Topology(this);

        allowThread();

        Debug.assertion(TransactedObject.Id.NULL.Value == 0);
        _nextObjectId = 1;

        synchronized (getLock()) {
            ObjectModel model = local.getDefaultObjectModel();
            _knownObjectModels.put(model.getUID(), model);

            registerAsTopologyInvolved(local.getOpenShares());
            _topology.addSite(local);
        }
    }

    public Site getLocalSite() {
        return _local;
    }

    public long getVersionNumber() {
        return _versionNumber;
    }

    public boolean idle() {
        boolean idle = true;

        synchronized (getLock()) {
            if (_transactions.size() > 0)
                idle = false;

            if (!_writeTrail.idle())
                idle = false;

            if (!_topology.idle())
                idle = false;

            if (_intercepted.size() > 0)
                idle = false;

            if (_internalListeners.size() > 0)
                idle = false;

            if (_siteListeners.size() > 0)
                idle = false;
        }

        return idle;
    }

    public Object getLock() {
        return _transactions;
    }

    public HashSet<Transaction> getTransactions() {
        return _transactions;
    }

    public Queue<Transaction> getInterceptedTransactions() {
        return _intercepted;
    }

    public Topology getTopology() {
        return _topology;
    }

    public void registerObjectModel(ObjectModel model) {
        assertThreadHoldsLock();

        _knownObjectModels.put(model.getUID(), model);

        for (Connection connection : getTopology().getConnections()) {
            if (connection instanceof Transport) {
                try {
                    ((Transport) connection).onObjectModelRegistered(model);
                } catch (IOException e) {
                    // Ignore, notification is best effort
                }
            }
        }
    }

    public ObjectModel getKnownObjectModel(String uid) {
        assertThreadHoldsLock();
        return _knownObjectModels.get(uid);
    }

    public Collection<ObjectModel> getKnownObjectModels() {
        assertThreadHoldsLock();
        return _knownObjectModels.values();
    }

    //

    public Transaction startTransaction() {
        return startTransaction(null, null, true);
    }

    public Transaction startTransaction(Long forcedId, Long forcedVersionOnStart, boolean register) {
        if (register && Transaction.getCurrent() != null)
            Transaction.throwAlreadyRunning();

        Transaction transaction;

        synchronized (getLock()) {
            assertThreadIsAllowed();
            long id = forcedId == null ? _nextTransactionId++ : forcedId;
            long versionOnStart = forcedVersionOnStart == null ? _versionNumber : forcedVersionOnStart;
            transaction = getLocalSite().createTransaction(id, versionOnStart);
            transaction.setStatus(Transaction.Status.RUNNING);

            if (register)
                _transactions.add(transaction);
        }

        if (register)
            Transaction.setCurrent(transaction);

        return transaction;
    }

    public void runTransacted(Runnable runnable) {
        Transaction transaction = startTransaction();
        runnable.run();
        transaction.beginCommit(new Callback(runnable));
    }

    private final class Callback implements CommitCallback {

        private final Runnable _runnable;

        public Callback(Runnable runnable) {
            _runnable = runnable;
        }

        public void onResult(Status result) {
            if (result == Transaction.Status.ABORTED)
                runTransacted(_runnable);
        }
    }

    public int getPendingCommitCount() {
        int value;

        synchronized (getLock()) {
            value = _intercepted.size();
        }

        return value;
    }

    public CommitInterception beginCommit(Transaction transaction, CommitCallback callback, boolean allowIntercept) {
        if (Transaction.getCurrent() != transaction)
            Transaction.throwNotRunning();

        CommitInterception interception = null;
        Transaction[] invalidated = null;

        try {
            synchronized (getLock()) {
                assertThreadIsAllowed();

                if (allowIntercept) {
                    while (getPendingCommitCount() >= Site.MAX_PENDING_COMMIT_COUNT) {
                        try {
                            getLock().wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                if (transaction.getStatus() != Transaction.Status.RUNNING) {
                    Debug.assertion(transaction.getStatus() == Transaction.Status.ABORTED);
                } else {
                    if (!_transactions.remove(transaction))
                        Debug.assertion(false);

                    if (!transaction.updateDependenciesAndReturnIfStillValid())
                        invalidated = applyStateChange(transaction, Transaction.Status.ABORTED);
                    else if (!transaction.checkConflictsAndReturnIfStillValid(_writeTrail))
                        invalidated = applyStateChange(transaction, Transaction.Status.ABORTED);
                    else {
                        if (transaction.modifiesTopology() || getTopology().isInvalid())
                            getTopology().recompute(transaction);

                        getTopology().prepareConnections(transaction);

                        Connection interceptor = null;

                        for (Connection c : getTopology().getConnections()) {
                            if (c.involved()) {
                                if (c.interceptsCommits()) {
                                    if (interceptor != null) {
                                        String message = "Transaction span over two interceptors (";
                                        message += interceptor + " and " + c + "). A possible cause is that ";
                                        message += "this transaction modifies objects contained in multiple shares, and ";
                                        message += "at least two of those shares are in conflict. Maybe those two shares ";
                                        message += "are connected to different servers.";
                                        transaction.setAbortReason(message);
                                        invalidated = applyStateChange(transaction, Transaction.Status.ABORTED);
                                        break;
                                    }

                                    interceptor = c;
                                }
                            }
                        }

                        if (transaction.getStatus() == Transaction.Status.RUNNING) {
                            if (interceptor == null || !allowIntercept) {
                                HashSet<Transaction> dependencies = transaction.getConfirmedDependencies();
                                Debug.assertion(dependencies == null || dependencies.size() == 0);

                                invalidated = applyStateChange(transaction, Transaction.Status.COMMITTED);
                            } else {
                                interception = new Interception(transaction, callback);
                                interceptor.onInterception(interception);
                                _intercepted.push(transaction);
                                invalidated = applyStateChange(transaction, Transaction.Status.SUSPENDED);
                            }
                        } else
                            Debug.assertion(transaction.getStatus() == Transaction.Status.ABORTED);
                    }
                }
            }

            // Finally does not pass jad (?) so this will be needed until
            // release of GWT 1.5

            Transaction.setCurrent(null);
        } catch (Throwable t) {
            Transaction.setCurrent(null);
            t.printStackTrace();
            throw new RuntimeException(t);
        }

        if (invalidated != null)
            for (Transaction current : invalidated)
                onStatusChanged(current);

        if (interception == null && allowIntercept) {
            onStatusChanged(transaction);

            if (callback != null)
                callback.onResult(transaction.getStatus());

            return null;
        }

        return interception;
    }

    private final class Interception extends CommitInterception {

        Transaction[] _invalidated;

        public Interception(Transaction transaction, CommitCallback callback) {
            super(transaction, callback);
        }

        @Override
        public long onResultLocked(Transaction.Status result) {
            assertThreadHoldsLock();

            Transaction transaction = _intercepted.pop();
            getLock().notifyAll();

            Debug.assertion(getTransaction() == transaction);
            Debug.assertion(getTransaction().getStatus() == Status.SUSPENDED);

            if (result != null) {
                _invalidated = applyStateChange(getTransaction(), result);

                if (result == Transaction.Status.COMMITTED)
                    return _versionNumber;
            }

            return 0;
        }

        @Override
        public void onResultUnlocked() {
            if (Debug.Level > 0) {
                synchronized (getLock()) {
                    assertThreadIsAllowed();
                }
            }

            if (_invalidated != null)
                for (Transaction current : _invalidated)
                    onStatusChanged(current);

            onStatusChanged(getTransaction());

            if (_callback != null)
                _callback.onResult(getTransaction().getStatus());

            if (_sync != null) {
                synchronized (_sync) {
                    _sync.notifyAll();
                }
            }
        }
    }

    public void suspend(Transaction transaction) {
        if (Transaction.getCurrent() != transaction)
            Transaction.throwNotRunning();

        Transaction[] invalidated = null;

        synchronized (getLock()) {
            assertThreadIsAllowed();

            if (transaction.getStatus() != Transaction.Status.RUNNING)
                Debug.assertion(transaction.getStatus() == Transaction.Status.ABORTED);
            else
                invalidated = applyStateChange(transaction, Transaction.Status.SUSPENDED);
        }

        Transaction.setCurrent(null);

        if (invalidated != null)
            for (Transaction current : invalidated)
                onStatusChanged(current);

        if (transaction.getStatus() == Transaction.Status.SUSPENDED)
            onStatusChanged(transaction);
    }

    public void tryToResume(Transaction transaction) {
        if (Transaction.getCurrent() != null)
            Transaction.throwAlreadyRunning();

        Transaction[] invalidated = null;

        synchronized (getLock()) {
            assertThreadIsAllowed();

            String message = "You can only resume suspended transactions.";

            if (transaction.getStatus() == Transaction.Status.RUNNING)
                throw new IllegalStateException("Transaction is running." + message);

            if (transaction.getStatus() == Transaction.Status.COMMITTED)
                throw new IllegalStateException("Transaction has commited." + message);

            if (transaction.getStatus() == Transaction.Status.SUSPENDED)
                invalidated = applyStateChange(transaction, Transaction.Status.RUNNING);

            // Do nothing if aborted since user can't test before calling
            // this method. A transaction can be aborted any time.
        }

        if (invalidated != null)
            for (Transaction current : invalidated)
                onStatusChanged(current);

        if (transaction.getStatus() == Transaction.Status.RUNNING) {
            Transaction.setCurrent(transaction);
            onStatusChanged(transaction);
        }
    }

    public void abort(Transaction transaction, boolean silent) {
        if (Transaction.getCurrent() != transaction)
            Transaction.throwNotRunning();

        Transaction[] invalidated = null;

        synchronized (getLock()) {
            assertThreadIsAllowed();

            if (transaction.getStatus() != Transaction.Status.RUNNING) {
                Debug.assertion(transaction.getStatus() == Transaction.Status.ABORTED);
            } else {
                if (!_transactions.remove(transaction))
                    Debug.assertion(false);

                invalidated = applyStateChange(transaction, Transaction.Status.ABORTED);
            }
        }

        Transaction.setCurrent(null);

        if (invalidated != null)
            for (Transaction current : invalidated)
                onStatusChanged(current);

        if (!silent)
            onStatusChanged(transaction);
    }

    private Transaction[] applyStateChange(Transaction transaction, Transaction.Status state) {
        if (state == Transaction.Status.COMMITTED) {
            for (int i = 0; i < _internalListeners.size(); i++)
                _internalListeners.get(i).onCommitting(transaction);

            _versionNumber++;

            transaction.commitObjects(_versionNumber);

            for (Connection connections : getTopology().getConnections())
                connections.onCommit(transaction, _versionNumber);

            for (int i = 0; i < _internalListeners.size(); i++)
                _internalListeners.get(i).onCommitted(transaction, _versionNumber);
        }

        if (state == Transaction.Status.ABORTED) {
            for (Connection connection : getTopology().getConnections())
                connection.onAbort(transaction);

            for (int i = 0; i < _internalListeners.size(); i++)
                _internalListeners.get(i).onAborted(transaction);

            if (transaction.modifiesTopology())
                getTopology().markAsInvalid();
        }

        transaction.setStatus(state);

        if (state == Transaction.Status.COMMITTED) {
            if (transaction.getPrivateObjects() != null)
                _writeTrail.onCommit(transaction);
        }

        return pruneWriteTrailHavingTheLock();
    }

    public void onStatusChanged(Transaction transaction) {
        if (Debug.Level > 0)
            Debug.assertion(!Thread.holdsLock(getLock()));

        if (transaction.hasListeners())
            transaction.raiseStatusChanged();

        if (transaction.getStatus() == Transaction.Status.COMMITTED) {
            for (Site.Listener listener : Utils.values(_siteListeners))
                listener.onCommitted(transaction);
        }

        if (transaction.getStatus() == Transaction.Status.ABORTED) {
            for (Site.Listener listener : Utils.values(_siteListeners))
                listener.onAborted(transaction);
        }
    }

    public void pruneWriteTrail() {
        if (Debug.Level > 0)
            Debug.assertion(!Thread.holdsLock(getLock()));

        Transaction[] invalidated;

        synchronized (getLock()) {
            invalidated = pruneWriteTrailHavingTheLock();
        }

        if (invalidated != null)
            for (Transaction current : invalidated)
                onStatusChanged(current);
    }

    private Transaction[] pruneWriteTrailHavingTheLock() {
        long minVersion = Long.MAX_VALUE;

        for (Connection connection : getTopology().getConnections()) {
            Long version = connection.getAcknowledgedVersion();

            if (version != null && version < minVersion)
                minVersion = version;
        }

        // Uncomment to disable pruning
        // minVersion = 0;

        HashSet<Transaction> invalidated = _writeTrail.pruneWritesTrail(minVersion);

        for (Transaction current : invalidated) {
            if (!_transactions.remove(current))
                Debug.assertion(false);

            Debug.assertion(current.getStatus() == Transaction.Status.SUSPENDED);
            current.setStatus(Transaction.Status.ABORTED);
        }

        if (invalidated.size() > 0)
            return invalidated.toArray(new Transaction[invalidated.size()]);

        return null;
    }

    public void register(TransactedObject o) {
        Debug.assertion(!(o instanceof Group || o instanceof Share));
        register(o, false);
    }

    public void registerAsTopologyInvolved(TransactedObject o) {
        register(o, true);
    }

    private void register(TransactedObject o, boolean negative) {
        assertThreadHoldsLock();

        Debug.assertion(!(o instanceof Site));

        int id;

        if (_freeObjectIds.size() > 0)
            id = _freeObjectIds.remove(_freeObjectIds.size() - 1).Value;
        else
            id = _nextObjectId++;

        Debug.assertion(_local.getObject(id) == null, "Not null at " + id + ": " + _local.getObject(id));
        _local.setObject(negative ? -id : id, o);
    }

    // private void unregister(TransactedObject o) {
    // assertThreadHoldsLock();
    //
    // Debug.assertion(o.getOrigin() != null && o.getId() != 0);
    // Debug.assertion(!(o instanceof Site));
    //
    // int id = o.getId();
    // o.setId(TransactedObject.Id.DISCONNECTED.Value);
    //
    // Debug.assertion(_local.getObject(id) == o);
    // _local.setNull(id);
    //
    // if (id > 0)
    // _freeObjectIds.add(new TransactedObject.Id(id));
    // }

    //

    public void addInternalListener(Site.InternalListener listener) {
        Debug.assertion(listener != null);

        synchronized (getLock()) {
            _internalListeners.add(listener);
        }
    }

    public void removeInternalListener(Site.InternalListener listener) {
        Debug.assertion(listener != null);

        synchronized (getLock()) {
            _internalListeners.remove(listener);
        }
    }

    public void addSiteListener(Object key, Site.Listener listener) {
        Debug.assertion(key != null && listener != null);
        Debug.assertion(!_siteListeners.containsKey(key));
        _siteListeners.put(key, listener);
    }

    public void addSiteListener(Site.Listener listener) {
        addSiteListener(listener, listener);
    }

    public void removeTransactionListener(Object key) {
        Debug.assertion(key != null);
        Debug.assertion(_siteListeners.containsKey(key));
        _siteListeners.remove(key);
    }

    public void removeTransactionListener(Site.Listener listener) {
        removeTransactionListener((Object) listener);
    }

    //

    public void assertThreadHoldsLock() {
        if (Debug.Level > 0) {
            assertThreadIsAllowed();
            Debug.assertion(Thread.holdsLock(getLock()));
        }
    }

    public void allowThread() {
        if (Debug.Level > 0) {
            synchronized (getLock()) {
                if (_allowedThreads == null)
                    _allowedThreads = new HashSet<Thread>();

                _allowedThreads.add(Thread.currentThread());
            }
        }
    }

    public void assertThreadIsAllowed() {
        if (Debug.Level > 0) {
            boolean ok = false;
            ArrayList<Thread> _done = null;

            Thread currentThread = Thread.currentThread();

            for (Thread thread : _allowedThreads) {
                if (thread == currentThread)
                    ok = true;

                if (thread.getState() == Thread.State.TERMINATED) {
                    if (_done == null)
                        _done = new ArrayList<Thread>();

                    _done.add(thread);
                }
            }

            if (_done != null)
                for (Thread thread : _done)
                    _allowedThreads.remove(thread);

            Debug.assertion(ok);
        }
    }
}
