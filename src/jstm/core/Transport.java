/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import jstm.misc.*;

/**
 * Connection dedicated to networking.
 */
public abstract class Transport extends Connection {

    public static final class Status { // Java enums cause pblms with gwt

        public static final Status DISCONNECTED = new Status("Disconnected");

        public static final Status CONNECTING = new Status("Connecting");

        public static final Status SYNCHRONIZING = new Status("Synchronizing");

        public static final Status SYNCHRONIZED = new Status("Synchronized");

        private final String _name;

        private Status(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    public interface Listener {

        void onDisconnected(IOException ex);

        void onStatusChanged();
    }

    protected static final byte REQUEST_INITIAL_OBJECTS = 1;

    protected static final byte INITIAL_OBJECTS = 2;

    protected static final byte COMMIT = 3;

    protected static final byte REQUEST_COMMIT = 4;

    protected static final byte COMMIT_RESULT = 5;

    protected static final byte COMMIT_RESULT_MODIFIED = 6;

    protected static final byte REPLICATION_REPORT = 7;

    protected static final byte OBJECT_MODEL_IS_KNOWN = 8;

    private final Connection _parent;

    private final HashSet<String> _objectModelsKnownByRemoteSite = new HashSet<String>();

    private final CopyOnWriteArrayList<Listener> _listeners = new CopyOnWriteArrayList<Listener>();

    private volatile Status _status = Status.DISCONNECTED;

    private Reader _reader;

    private Writer _writer;

    protected Transport(Site localSite) {
        super(localSite);

        _parent = null;
    }

    protected Transport(Connection parent) {
        super(parent.getLocalSite());

        _parent = parent;
    }

    protected final Connection getParent() {
        return _parent;
    }

    public final Status getStatus() {
        return _status;
    }

    protected final void assertStatus(Status expected) {
        Status status = getStatus();

        if (status != expected)
            throw new IllegalStateException("Status must be " + expected + " (Current value: " + status + ")");
    }

    protected void setStatus(Status value) {
        if (Debug.Level > 0)
            Debug.assertion(!Thread.holdsLock(getManager().getLock()));

        _status = value;

        OverrideAssert.add(this);
        onStatusChanged(value);
        OverrideAssert.end(this);
    }

    protected void onStatusChanged(Status value) {
        OverrideAssert.set(this);

        for (Listener listener : _listeners)
            listener.onStatusChanged();
    }

    public final void addListener(Listener listener) {
        _listeners.add(listener);
    }

    public final void removeListener(Listener listener) {
        _listeners.remove(listener);
    }

    //

    protected final Reader getReader() {
        return _reader;
    }

    protected final Writer getWriter() {
        return _writer;
    }

    protected abstract BinaryReader.Filler createFiller() throws IOException;

    protected abstract BinaryWriter.Flusher createFlusher() throws IOException;

    protected final void createReaderAndWriter() throws IOException {
        Debug.assertion(_writer == null && _reader == null);

        _writer = new Writer(this, createFlusher());
        _reader = new Reader(this, createFiller());
    }

    protected final void requestInitialObjects() throws IOException {
        synchronized (getManagerLock()) {
            for (ObjectModel model : getManager().getKnownObjectModels())
                getWriter().writeObjectModelIsKnown(model);

            getWriter().writeRequestInitialObjects();
        }
    }

    protected abstract void sendInitialObjects() throws IOException;

    @Override
    protected void _dispose(IOException ex) {
        super._dispose(ex);

        setStatus(Status.DISCONNECTED);

        for (Listener listener : _listeners)
            listener.onDisconnected(ex);

        _writer = null;
        _reader = null;
    }

    //

    protected final void onObjectModelRegistered(ObjectModel model) throws IOException {
        if (getWriter() != null)
            getWriter().writeObjectModelIsKnown(model);
    }

    //

    protected abstract void receivedInitialObjects(Transaction transaction);

    protected abstract void receivedCommit(Transaction transaction, long newServerVersion);

    protected abstract void receivedCommitRequest(Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies);

    protected abstract void receivedCommitResult(long id, boolean success, long newServerVersion);

    protected abstract void receivedCommitResultModified(long id, Transaction transaction, long newServerVersion);

    protected abstract void receivedReplicationReport(long replicatedServerVersion, long replicatedTransactionId);

    protected final void receivedObjectModelIsKnown(String uid) {
        synchronized (getManagerLock()) {
            _objectModelsKnownByRemoteSite.add(uid);
        }
    }

    protected final boolean getObjectModelIsKnownByRemoteSite(String uid) {
        getManager().assertThreadHoldsLock();
        return _objectModelsKnownByRemoteSite.contains(uid);
    }

    // Accessors for derived classes

    protected final Object getManagerLock() {
        return getManager().getLock();
    }

    protected final void assertThreadHoldsManagerLock() {
        getManager().assertThreadHoldsLock();
    }

    /**
     * Only for GWT
     */
    protected final void updateSiteUid(String uid) {
        getLocalSite().setUid(uid);
    }

    protected final void pruneWriteTrail() {
        getManager().pruneWriteTrail();
    }

    protected final void propagateCommit(Transaction transaction) {
        getManager().beginCommit(transaction, null, false);
    }

    protected final void abortSilently(Transaction transaction) {
        getManager().abort(transaction, true);
    }

    protected final void onStatusChanged(Transaction transaction) {
        transaction.getManager().onStatusChanged(transaction);
    }

    protected final boolean hasCalls(Transaction transaction) {
        return transaction.getCalls() != null;
    }

    protected final void runCalls(Transaction transaction) {
        if (transaction.getCalls() != null)
            for (MethodCall call : transaction.getCalls())
                call.run();
    }

    @SuppressWarnings("unchecked")
    protected final void copyContent(Transaction source, Transaction target) {
        if (source.getPrivateObjects() != null)
            target.replacePrivateObjects(source.getPrivateObjects());

        if (source.getCalls() != null)
            for (int i = 0; i < source.getCalls().size(); i++)
                target.getCalls().get(i).Result = source.getCalls().get(i).Result;
    }

    @SuppressWarnings("unchecked")
    protected final void endCalls(Transaction transaction) {
        if (transaction.getCalls() != null) {
            for (MethodCall call : transaction.getCalls()) {
                if (call._callback != null) {
                    if (transaction.getStatus() == Transaction.Status.ABORTED)
                        call._callback.onTransactionAborted();
                    else if (call.Result instanceof MethodCall.Error) {
                        String message = ((MethodCall.Error) call.Result).Message;
                        call._callback.onException(message);
                    } else
                        call._callback.onResult(call.Result);
                }
            }
        }
    }

    protected final Transaction createFakeTransaction() {
        return getManager().startTransaction(null, null, false);
    }

    // Logs

    protected static void logRequestInitialObjects(Site site, boolean read) {
        Debug.log(site + " " + (read ? "reads" : "writes") + " initial objects request");
    }

    protected static void logInitialObjects(Site site, boolean read, Transaction transaction) {
        Debug.log(site + " " + (read ? "reads" : "writes") + " initial objects (" + transaction + ")");
    }

    protected static void logCommit(Site site, boolean read, Transaction transaction, long newServerVersion) {
        Debug.log(site + " " + (read ? "reads" : "writes") + " commit (" + transaction + ", newServerVersion: " + newServerVersion + ")");
    }

    protected static void logCommitRequest(Site site, boolean read, Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies) {
        String s = site + " " + (read ? "reads" : "writes") + " commit request (" + transaction;
        s += ", intercepted at: " + replicatedServerVersionOnInterception + ", dependencies: " + Utils.toString(dependencies) + ")";
        Debug.log(s);
    }

    protected static void logCommitResult(Site site, boolean read, long id, boolean success, long newServerVersion) {
        String s = site + " " + (read ? "reads" : "writes") + " commit result " + id;
        s += " (success: " + success + ", newServerVersion: " + newServerVersion + ")";
        Debug.log(s);
    }

    protected static void logCommitResultModified(Site site, boolean read, long id, Transaction transaction, long newServerVersion) {
        String s = site + " " + (read ? "reads" : "writes") + " commit result modified " + id;
        s += " (transaction: " + transaction + ", newServerVersion: " + newServerVersion + ")";
        Debug.log(s);
    }

    protected static void logReplicationReport(Site site, boolean read, long replicatedServerVersion, long replicatedTransactionId) {
        String s = site + " " + (read ? "reads" : "writes") + " replication report";
        s += " (version: " + replicatedServerVersion + ", transaction: " + replicatedTransactionId + ")";
        Debug.log(s);
    }

    protected static void logObjectModelIsKnown(Site site, boolean read, String uid) {
        Debug.log(site + " " + (read ? "reads" : "writes") + " object model is known (" + uid + ")");
    }
}
