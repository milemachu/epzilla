/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import jstm.misc.Debug;

public final class Transaction {

    public static interface Listener {

        void onNewChange(Change change);

        void onStatusChanged(Transaction transaction);
    }

    public static final class Status { // Java enums cause pblms with gwt

        public static final Status RUNNING = new Status("Running");

        public static final Status SUSPENDED = new Status("Suspended");

        public static final Status COMMITTED = new Status("Committed");

        public static final Status ABORTED = new Status("Aborted");

        private final String _name;

        private Status(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    public static final class AbortedException extends RuntimeException {

        public AbortedException() {
            super("This transaction has been aborted.");
        }
    }

    private final TransactionManager _manager;

    private final long _id;

    private long _versionOnStart;

    private long _resultingVersion;

    private Status _status;

    private HashMap<TransactedObject, TransactedObject.Version> _privateObjects;

    private ArrayList<MethodCall> _calls;

    private final Transaction[] _potentialDependencies;

    private HashSet<Transaction> _confirmedDependencies;

    private Site _origin;

    private String _abortReason;

    private HashMap<Object, Object> _tags;

    private boolean _modifiesTopology;

    private boolean _checkedAssertion;

    private ArrayList<Listener> _listeners;

    private static final ThreadLocal<Transaction> _threadCurrentTransaction = new ThreadLocal<Transaction>();

    protected Transaction(Site local, long id, long versionOnStart) {
        local.getManager().assertThreadHoldsLock();

        _manager = local.getManager();
        _id = id;
        _versionOnStart = versionOnStart;

        int length = _manager.getInterceptedTransactions().size();

        if (length > 0) {
            _potentialDependencies = new Transaction[length];

            for (int i = 0; i < _potentialDependencies.length; i++)
                _potentialDependencies[i] = _manager.getInterceptedTransactions().get(length - 1 - i);
        } else
            _potentialDependencies = null;

        _origin = _manager.getLocalSite();
    }

    public static Transaction getCurrent() {
        return _threadCurrentTransaction.get();
    }

    protected static void setCurrent(Transaction transaction) {
        _threadCurrentTransaction.set(transaction);
    }

    protected TransactionManager getManager() {
        return _manager;
    }

    public long getId() {
        return _id;
    }

    public Status getStatus() {
        return _status;
    }

    protected boolean isActive() {
        return getStatus() == Status.RUNNING || getStatus() == Status.SUSPENDED;
    }

    protected void setStatus(Status state) {
        Debug.assertion(_status != Status.COMMITTED);

        if (_status == Status.ABORTED)
            Debug.assertion(state == Status.ABORTED);
        else
            Debug.assertion(state != _status, "Updating to same state : " + state);

        _status = state;
    }

    public void suspend() {
        _manager.suspend(this);
    }

    public void resume() throws AbortedException {
        Status state = tryToResume();

        if (state != Status.RUNNING) {
            Debug.assertion(state == Status.ABORTED);
            throw new AbortedException();
        }
    }

    public Status tryToResume() {
        _manager.tryToResume(this);
        return getStatus();
    }

    public void commit() throws AbortedException {
        Status state = tryToCommit();

        if (state != Status.COMMITTED) {
            Debug.assertion(state == Status.ABORTED);
            throw new AbortedException();
        }
    }

    public Status tryToCommit() {
        CommitInterception future = _manager.beginCommit(this, null, true);

        if (future != null) {
            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            }
        }

        return getStatus();
    }

    public Future<Status> beginCommit(CommitCallback callback) {
        return _manager.beginCommit(this, callback, true);
    }

    public void abort() {
        abort(null);
    }

    public void abort(String reason) {
        setAbortReason(reason);
        _manager.abort(this, false);
    }

    protected void abortSilently() {
        _manager.abort(this, true);
    }

    public HashMap<TransactedObject, TransactedObject.Version> getPrivateObjects() {
        return _privateObjects;
    }

    public HashMap<TransactedObject, TransactedObject.Version> getOrCreatePrivateObjects() {
        Debug.assertion(getStatus() == Status.RUNNING);

        if (_privateObjects == null)
            _privateObjects = new HashMap<TransactedObject, TransactedObject.Version>();

        return _privateObjects;
    }

    protected void replacePrivateObjects(HashMap<TransactedObject, TransactedObject.Version> map) {
        Debug.assertion(getStatus() == Status.SUSPENDED);
        _privateObjects = map;
    }

    protected TransactedObject.Version getVersionIfExists(TransactedObject object) {
        HashMap<TransactedObject, TransactedObject.Version> versions = getPrivateObjects();

        if (versions != null)
            return versions.get(object);

        return null;
    }

    protected TransactedObject.Version getVersion(TransactedObject object) {
        HashMap<TransactedObject, TransactedObject.Version> versions;

        if (getStatus() == Transaction.Status.RUNNING)
            versions = getOrCreatePrivateObjects();
        else
            versions = getPrivateObjects();

        if (versions != null) {
            TransactedObject.Version version = versions.get(object);

            if (version == null && getStatus() == Transaction.Status.RUNNING)
                versions.put(object, version = object.createVersion(this));

            return version;
        }

        return null;
    }

    protected Transaction[] getPotentialDependencies() {
        return _potentialDependencies;
    }

    public HashSet<Transaction> getConfirmedDependencies() {
        Debug.assertion(isActive());
        return _confirmedDependencies;
    }

    public void confirmDependency(Transaction dependency) {
        Debug.assertion(dependency.getStatus() != Status.RUNNING);

        if (getStatus() == Status.RUNNING) {
            if (_confirmedDependencies == null)
                _confirmedDependencies = new HashSet<Transaction>();

            Debug.assertion(!_checkedAssertion);
            _confirmedDependencies.add(dependency);
        }
    }

    public HashSet<Transaction> getOrCreateConfirmedDependencies() {
        Debug.assertion(getStatus() == Status.RUNNING);
        Debug.assertion(!_checkedAssertion);

        if (_confirmedDependencies == null)
            _confirmedDependencies = new HashSet<Transaction>();

        return _confirmedDependencies;
    }

    public long getVersionOnStart() {
        return _versionOnStart;
    }

    public long getResultingVersion() {
        Debug.assertion(getStatus() == Transaction.Status.COMMITTED || getStatus() == Transaction.Status.SUSPENDED, getStatus().toString());
        return _resultingVersion;
    }

    public Site getOrigin() {
        return _origin;
    }

    protected void setOrigin(Site value) {
        _origin = value;
    }

    public String getAbortReason() {
        return _abortReason;
    }

    protected void setAbortReason(String value) {
        _abortReason = value;
    }

    protected boolean modifiesTopology() {
        return _modifiesTopology;
    }

    protected void setModifiesTopology() {
        _modifiesTopology = true;
    }

    /**
     * Properties of the transaction. If the key and and the value are Strings
     * or TransactedObjects, they will be serialized with the transaction in
     * distributed senarios.
     */
    public Map<Object, Object> getTags() {
        return _tags;
    }

    public Object getTag(Object key) {
        if (_tags != null)
            return _tags.get(key);

        return null;
    }

    public void setTag(Object key, Object value) {
        if (_tags == null)
            _tags = new HashMap<Object, Object>();

        _tags.put(key, value);
    }

    protected void setTags(HashMap<Object, Object> value) {
        _tags = value;
    }

    @Override
    public String toString() {
        return "" + getId() + ", " + _tags + " (" + getStatus() + ")";
    }

    //

    public void addListener(Listener listener) {
        if (_listeners == null)
            _listeners = new ArrayList<Listener>();

        _listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        if (_listeners != null) {
            _listeners.remove(listener);

            if (_listeners.size() == 0)
                _listeners = null;
        }
    }

    protected boolean hasListeners() {
        return _listeners != null;
    }

    protected void raiseNewChange(Change change) {
        if (Debug.Level > 0)
            Debug.assertion(!Thread.holdsLock(getManager().getLock()));

        for (Transaction.Listener listener : _listeners)
            listener.onNewChange(change);
    }

    protected void raiseStatusChanged() {
        if (Debug.Level > 0)
            Debug.assertion(!Thread.holdsLock(getManager().getLock()));

        for (Transaction.Listener listener : _listeners)
            listener.onStatusChanged(this);
    }

    //

    protected static Transaction startIfNeeded(Transaction transaction) {
        if (transaction == null) {
            return Site.getLocal().startTransaction();
        } else {
            Transaction.throwIfNotRunning(transaction);
            return transaction;
        }
    }

    protected static void throwAlreadyRunning() {
        String message = "A transaction is already running on this thread. ";
        message += "You probably tried to start two transactions on the same thread ";
        message += "or resumed a transaction while another was already running.";
        throw new RuntimeException(message);
    }

    protected static void throwNotRunning() {
        String message = "This transaction is not currently running on this thread. ";
        message += "You probably have already committed, aborted or suspended this transaction.";
        throw new RuntimeException(message);
    }

    protected static void throwIfNotRunning(Transaction transaction) {
        if (transaction == null || transaction.getStatus() != Transaction.Status.RUNNING) {
            String message = "Current transaction is null or not running. It has ";
            message += "probably been aborted due to a conflict with another transaction.";
            throw new IllegalStateException(message);
        }
    }

    protected static void throwIfCurrentNotNull() {
        if (getCurrent() != null) {
            String message = "Current transaction must be null. This method cannot be called in a ";
            message += "transaction. Please ensure you have commited, suspended or aborted all ";
            message += "transactions on this thread before calling it.";
            throw new IllegalStateException(message);
        }
    }

    protected boolean invalidates(Transaction transaction) {
        Debug.assertion(transaction.isActive(), "State: " + transaction.getStatus());

        // A conflict occur only if a field is written after the start of the
        // transaction

        if (getResultingVersion() > transaction.getVersionOnStart()) {
            HashMap<TransactedObject, TransactedObject.Version> versions = transaction.getPrivateObjects();

            if (versions != null) {
                for (Map.Entry<TransactedObject, TransactedObject.Version> entry : versions.entrySet()) {
                    TransactedObject.Version our = _privateObjects.get(entry.getKey());

                    if (our != null && our.invalidates(entry.getValue()))
                        return true;
                }
            }
        }// else
        // Debug.log("Skipped test if " + getInfo() + " (" +
        // getResultingCommitCount() + ") invalidates " +
        // transaction.getInfo());

        return false;
    }

    protected void addCallsAsNewObjectsToConnections() {
        if (getCalls() != null) {
            for (MethodCall call : getCalls()) {
                if (call.Result != null) {
                    Connection connection = getOrigin().getRoute();

                    if (connection != null) {
                        connection.notifyHasCalls();

                        addLocalAsNewObjectsToConnections(connection, call.Result);
                    }
                } else {
                    Connection connection = null;

                    if (call.Structure.getOrigin() != null)
                        connection = call.Structure.getOrigin().getRoute();

                    if (connection != null) {
                        connection.notifyHasCalls();

                        for (Object o : call.Arguments)
                            addLocalAsNewObjectsToConnections(connection, o);
                    }
                }
            }
        }
    }

    private void addLocalAsNewObjectsToConnections(Connection connection, Object o) {
        if (o instanceof TransactedObject) {
            connection.addNew((TransactedObject) o, this);

            // TODO iterate over non transient references
        } else {
            if (o instanceof List)
                for (Object element : (List) o)
                    addLocalAsNewObjectsToConnections(connection, element);
        }
    }

    protected void commitObjects(long resultingCommitCount) {
        Debug.assertion(getStatus() == Status.RUNNING || getStatus() == Status.SUSPENDED);

        _resultingVersion = resultingCommitCount;

        if (getPrivateObjects() != null)
            for (Map.Entry<TransactedObject, TransactedObject.Version> entry : getPrivateObjects().entrySet())
                entry.getValue().commit();
    }

    private static final class CommitCount {

        public Long Value;
    }

    protected boolean updateDependenciesAndReturnIfStillValid() {
        CommitCount count = new CommitCount();
        return updateDependenciesAndReturnIfStillValid(count);
    }

    private boolean updateDependenciesAndReturnIfStillValid(CommitCount count) {
        if (_confirmedDependencies != null) {
            ArrayList<Transaction> committed = null;

            for (Transaction dependency : _confirmedDependencies) {
                if (dependency.getStatus() == Status.COMMITTED) {
                    if (committed == null)
                        committed = new ArrayList<Transaction>();

                    committed.add(dependency);

                    long result = dependency.getResultingVersion();

                    if (count.Value == null || count.Value < result) {
                        count.Value = result;
                    }
                } else if (dependency.isActive()) {
                    if (!dependency.updateDependenciesAndReturnIfStillValid(count))
                        return false;
                } else {
                    Debug.assertion(dependency.getStatus() == Status.ABORTED);
                    return false;
                }
            }

            if (committed != null) {
                for (Transaction dependency : committed)
                    _confirmedDependencies.remove(dependency);

                if (_confirmedDependencies.size() == 0) {
                    _confirmedDependencies = null;
                    _checkedAssertion = true;
                }
            }

            if (count.Value != null && count.Value > _versionOnStart)
                _versionOnStart = count.Value;
        }

        return true;
    }

    protected boolean checkConflictsAndReturnIfStillValid(WriteTrail writeTrail) {
        // ? if (getConfirmedDependencies() == null ||
        // getConfirmedDependencies().size() == 0)
        if (!writeTrail.checkIfStillValid(this))
            return false;

        // If transaction is invalidated by a dependency, it will always abort

        if (getConfirmedDependencies() != null)
            for (Transaction dependency : getConfirmedDependencies())
                if (dependency.invalidates(this))
                    return false;

        return true;
    }

    //

    protected ArrayList<MethodCall> getCalls() {
        return _calls;
    }

    protected ArrayList<MethodCall> getOrCreateCalls() {
        if (_calls == null)
            _calls = new ArrayList<MethodCall>();

        return _calls;
    }
}
