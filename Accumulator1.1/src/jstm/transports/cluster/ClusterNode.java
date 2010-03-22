/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.cluster;

import java.io.IOException;
import java.util.concurrent.*;

import jstm.core.*;
import jstm.misc.BinaryReader.Filler;
import jstm.misc.BinaryWriter.Flusher;

public class ClusterNode extends Transport {

    private ConnectFuture _connectFuture;

    private ConnectionInfo _connection;

    private Group _cluster;

    public ClusterNode() {
        this(Site.getLocal());
    }

    public ClusterNode(Site site) {
        super(site);
    }

    //

    private final class ConnectFuture implements Future<ConnectionInfo> {

        protected final MethodCallback<ConnectionInfo> _callback;

        protected ConnectionInfo _info;

        protected ConnectFuture(MethodCallback<ConnectionInfo> callback) {
            _callback = callback;
        }

        public boolean cancel(boolean flag) {
            throw new UnsupportedOperationException();
        }

        public ConnectionInfo get() throws InterruptedException, ExecutionException {
            try {
                return get(0, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            return null;
        }

        public ConnectionInfo get(long timeSpan, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            synchronized (getManagerLock()) {
                while (!isDone())
                    getManagerLock().wait(timeUnit.toMillis(timeSpan));
            }

            return _info;
        }

        public final boolean isCancelled() {
            return false;
        }

        public final boolean isDone() {
            return _info != null;
        }
    }

    @Override
    protected Filler createFiller() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Flusher createFlusher() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void receivedCommit(Transaction transaction, long newServerVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void receivedCommitRequest(Transaction transaction, long replicatedServerVersionOnInterception, long[] dependencies) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void receivedCommitResult(long id, boolean success, long newServerVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void receivedCommitResultModified(long id, Transaction transaction, long newServerVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void receivedInitialObjects(Transaction transaction) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void receivedReplicationReport(long replicatedServerVersion, long replicatedTransactionId) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void sendInitialObjects() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Long getAcknowledgedVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean interceptsCommits() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean interceptsCommitsCanChange() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onAbort(Transaction transaction) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onCommit(Transaction transaction, long newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onInterception(CommitInterception interception) {
        // TODO Auto-generated method stub

    }
}
