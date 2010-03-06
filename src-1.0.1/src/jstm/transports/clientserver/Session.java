/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver;

import java.io.IOException;
import java.util.Iterator;

import jstm.core.*;
import jstm.misc.Debug;
import jstm.misc.Queue;

public abstract class Session extends Transport {

    public static final int MAX_EXPECTED_PENDING_TRANSACTIONS = 1000;

    private final Queue<Transaction> _pendingTransactions = new Queue<Transaction>();

    private long _acknowledgedServerVersion;

    private Site _clientSite;

    protected Session(Server server) {
        super(server);
    }

    public Server getServer() {
        return (Server) getParent();
    }

    public Site getClientSite() {
        return _clientSite;
    }

    protected void start() throws IOException {
        setStatus(Status.CONNECTING);
        createReaderAndWriter();
        setStatus(Status.SYNCHRONIZING);
        requestInitialObjects();
    }

    protected final void stop() {
        dispose(null);
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZED)
            getServer().raiseConnected(this);
    }

    @Override
    protected void _dispose(IOException ex) {
        getServer().raiseDisconnected(this);

        super._dispose(ex);
    }

    @Override
    protected final void sendInitialObjects() throws IOException {
        synchronized (getManagerLock()) {
            getNew().clear();
            getShared().clear();

            Transaction transaction = createFakeTransaction();

            transaction.setTag(Server.SERVER, getLocalSite());
            transaction.setTag(Server.GROUP, getServer().getServerAndClients());

            // getNew().put(getLocalSite(), null);
            // getNew().put(getLocalSite().getOpenShares(), null);
            // getNew().put(getServer().getServerAndClients(), null);
            // getNew().put(getServer().getServerAndClients().getOpenShares(),
            // null);

            for (Share share : getServer().getServerAndClients().getOpenShares()) {
                // getNew().put(share, null);

                for (Iterator<TransactedObject> it2 = share.iterator(); it2.hasNext();)
                    addNewWithVersion(it2.next(), null);
            }

            getWriter().writeInitialObjects(this, transaction);
        }
    }

    // Local

    @Override
    protected final boolean interceptsCommits() {
        return false;
    }

    @Override
    protected final boolean interceptsCommitsCanChange() {
        return false;
    }

    @Override
    protected final void onInterception(CommitInterception interception) {
        Debug.fail("Not on server");
    }

    @Override
    protected final void onCommit(Transaction transaction, long newVersion) {
        if (involved() && !getServer().serverInvolved())
            sendCommit(this, transaction, newVersion);
    }

    protected final void sendCommit(Connection connection, Transaction transaction, long newVersion) {
        if (getStatus() == Status.SYNCHRONIZED) {
            Connection route = getRoute(transaction);
            boolean failed = false;

            try {
                if (this != route)
                    getWriter().writeCommit(connection, transaction, newVersion);
                else {
                    if (!hasCalls(transaction))
                        getWriter().writeCommitResult(transaction.getId(), true, newVersion);
                    else
                        getWriter().writeCommitResultModified(connection, transaction.getId(), transaction, newVersion);
                }
            } catch (Exception ex) {
                failed = true;
            }

            if (failed) // Out of catch to prevent blocking
                setStatus(Status.DISCONNECTED);
        }
    }

    @Override
    protected final void onAbort(Transaction transaction) {
        if (getStatus() == Status.SYNCHRONIZED) {
            if (getRoute(transaction) == this) {
                boolean failed = false;

                try {
                    getWriter().writeCommitResult(transaction.getId(), false, 0);
                } catch (Exception ex) {
                    failed = true;
                }

                if (failed) // Out of catch to prevent blocking
                    setStatus(Status.DISCONNECTED);
            }
        }
    }

    @Override
    protected final Long getAcknowledgedVersion() {
        return _acknowledgedServerVersion;
    }

    // Received

    @Override
    protected void receivedInitialObjects(Transaction transaction) {
        synchronized (getManagerLock()) {
            Debug.assertion(_clientSite == null);
            _clientSite = (Site) transaction.getTag(Server.CLIENT);
            Debug.assertion(_clientSite != null);
        }

        registerConnection(null);
        setStatus(Status.SYNCHRONIZED);
        getServer().raiseConnected(this);
    }

    @Override
    protected void receivedCommit(Transaction transaction, long newServerVersion) {
        Debug.fail("Not server side");
    }

    @Override
    protected void receivedCommitRequest(Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies) {
        if (dependencies != null && dependencies.length > 0) {
            for (Transaction current : _pendingTransactions)
                for (long id : dependencies)
                    if (id == current.getId())
                        transaction.getOrCreateConfirmedDependencies().add(current);

            Debug.assertion(transaction.getOrCreateConfirmedDependencies().size() == dependencies.length);
        }

        _pendingTransactions.push(transaction);

        if (_pendingTransactions.size() > MAX_EXPECTED_PENDING_TRANSACTIONS)
            System.out.println("Warning : _pendingTransactions is getting big (" + _pendingTransactions.size() + ")");

        runCalls(transaction);

        transaction.beginCommit(null);
    }

    @Override
    protected void receivedCommitResult(long id, boolean success, long newServerVersion) {
        Debug.fail("Not server side");
    }

    @Override
    protected void receivedCommitResultModified(long id, Transaction transaction, long newServerVersion) {
        Debug.fail("Not server side");
    }

    @Override
    protected void receivedReplicationReport(long replicatedServerVersion, long replicatedTransactionId) {
        _acknowledgedServerVersion = replicatedServerVersion;

        while (_pendingTransactions.size() > 0) {
            if (_pendingTransactions.first().getId() <= replicatedTransactionId) {
                Transaction transaction = _pendingTransactions.pop();

                if (Debug.MESSAGING)
                    Debug.log("popped " + transaction + " (" + replicatedServerVersion + ", " + replicatedTransactionId + ") from " + this);
            } else
                return;
        }

        pruneWriteTrail();
    }
}
