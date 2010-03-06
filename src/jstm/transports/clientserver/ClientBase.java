/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver;

import java.io.IOException;
import java.util.HashMap;

import jstm.core.*;
import jstm.misc.Debug;
import jstm.misc.ThreadPool;

public abstract class ClientBase extends Transport {

    private final HashMap<Long, CommitInterception> _pendingCommits = new HashMap<Long, CommitInterception>();

    private long _replicatedServerVersion, _replicatedServerVersionSent;

    private long _replicatedTransactionId, _replicatedTransactionIdSent;

    // Should be useful only if a lot of clients connected
    private volatile ThreadPool.Task _progressReportsSender;

    public ClientBase(Site localSite) {
        super(localSite);

        allowThread(localSite);
    }

    @Override
    protected final void sendInitialObjects() throws IOException {
        synchronized (getManagerLock()) {
            getNew().clear();
            getShared().clear();

            Transaction transaction = createFakeTransaction();
            transaction.setTag(Server.CLIENT, getLocalSite());

            for (Share share : getLocalSite().getOpenShares())
                getNew().put(share, null);

            getWriter().writeInitialObjects(this, transaction);
        }
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZED) {
            ProgressReportsSender sender = new ProgressReportsSender();
            _progressReportsSender = ThreadPool.scheduleAtFixedRate(sender, 10000);
        }
    }

    @Override
    protected void _dispose(IOException ex) {
        ThreadPool.Task task = _progressReportsSender;

        if (task != null)
            task.cancel();

        _progressReportsSender = null;

        super._dispose(ex);
    }

    // Overriden by NSTM
    protected ConnectionInfo createConnectionInfo(Site server, Group group) {
        return new ConnectionInfo(server, group);
    }

    // Local

    @Override
    protected final boolean interceptsCommits() {
        return true;
    }

    @Override
    protected final boolean interceptsCommitsCanChange() {
        return false;
    }

    @Override
    protected final void onInterception(CommitInterception interception) {
        Transaction tx = interception.getTransaction();
        long[] dependencies = null;

        if (tx.getConfirmedDependencies() != null) {
            dependencies = new long[tx.getConfirmedDependencies().size()];
            int i = 0;

            for (Transaction current : tx.getConfirmedDependencies())
                dependencies[i++] = current.getId();
        }

        _pendingCommits.put(tx.getId(), interception);

        try {
            getWriter().writeCommitRequest(this, tx, _replicatedServerVersion, dependencies);
        } catch (IOException ex) {
            dispose(ex);
        }
    }

    @Override
    protected final void onCommit(Transaction transaction, long newVersion) {
    }

    @Override
    protected final void onAbort(Transaction transaction) {
    }

    @Override
    protected final Long getAcknowledgedVersion() {
        return null;
    }

    protected abstract MethodCallback<ConnectionInfo> onFirstCommit(ConnectionInfo info);

    // Received

    @Override
    protected void receivedInitialObjects(Transaction transaction) {
        MethodCallback<ConnectionInfo> callback = null;
        Site server = (Site) transaction.getTag(Server.SERVER);
        Group group = (Group) transaction.getTag(Server.GROUP);
        ConnectionInfo connection = createConnectionInfo(server, group);

        registerConnection(group);
        callback = onFirstCommit(connection);
        setStatus(Status.SYNCHRONIZED);

        if (callback != null)
            callback.onResult(connection);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void receivedCommit(Transaction transaction, long newServerVersion) {
        synchronized (getManagerLock()) {
            propagateCommit(transaction);
            Debug.assertion(transaction.getStatus() == Transaction.Status.COMMITTED);
            replicatedServerVersion(newServerVersion);
        }

        onStatusChanged(transaction);
    }

    @Override
    protected void receivedCommitRequest(Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies) {
        Debug.fail("Not client side");
    }

    @Override
    protected void receivedCommitResult(long id, boolean success, long newServerVersion) {
        CommitInterception interception;

        synchronized (getManagerLock()) {
            interception = _pendingCommits.remove(id);
            Debug.assertion(id == interception.getTransaction().getId());
            Transaction.Status state = success ? Transaction.Status.COMMITTED : Transaction.Status.ABORTED;
            interception.onResultLocked(state);
            _replicatedTransactionId = id;

            if (success)
                replicatedServerVersion(newServerVersion);
        }

        interception.onResultUnlocked();
        endCalls(interception.getTransaction());
    }

    @Override
    protected void receivedCommitResultModified(long id, Transaction transaction, long newServerVersion) {
        CommitInterception interception;

        synchronized (getManagerLock()) {
            interception = _pendingCommits.remove(id);
            Debug.assertion(id == interception.getTransaction().getId());

            copyContent(transaction, interception.getTransaction());

            interception.onResultLocked(Transaction.Status.COMMITTED);
            _replicatedTransactionId = id;

            replicatedServerVersion(newServerVersion);
        }

        abortSilently(transaction);
        interception.onResultUnlocked();
        endCalls(interception.getTransaction());
    }

    private void replicatedServerVersion(long version) {
        // Server can send two messages for a transaction : one for the share
        // attached to the server, one for another share attached to the
        // session. MathodCalls can also be sent.
        Debug.assertion(_replicatedServerVersion == 0 || version >= _replicatedServerVersion);
        _replicatedServerVersion = version;

        // Client thread can prevent count to be sent to server if it
        // synchronizes too much on the lock, so force update from time to
        // time
        sendReportIfDeltaMoreThan(10);
    }

    protected void sendReportIfDeltaMoreThan(int max) {
        if (_replicatedServerVersion - _replicatedServerVersionSent > max || _replicatedTransactionId - _replicatedTransactionIdSent > max) {
            _replicatedServerVersionSent = _replicatedServerVersion;
            _replicatedTransactionIdSent = _replicatedTransactionId;

            try {
                getWriter().writeReplicationReport(_replicatedServerVersion, _replicatedTransactionId);
            } catch (IOException e) {
                // Ignore. Report is best effort.
            }
        }
    }

    @Override
    protected void receivedReplicationReport(long replicatedServerVersion, long replicatedTransactionId) {
        Debug.fail("Not client side");
    }

    private final class ProgressReportsSender implements Runnable {

        public void run() {
            allowThread(ClientBase.this.getLocalSite());

            synchronized (getManagerLock()) {
                sendReportIfDeltaMoreThan(1);
            }
        }
    }
}
