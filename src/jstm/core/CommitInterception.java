/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import java.util.concurrent.*;

import jstm.core.Transaction.Status;

public abstract class CommitInterception implements Future<Status> {

    protected final Transaction _transaction;

    protected final CommitCallback _callback;

    protected volatile Object _sync;

    protected CommitInterception(Transaction transaction, CommitCallback callback) {
        _transaction = transaction;
        _callback = callback;
    }

    public final Transaction getTransaction() {
        return _transaction;
    }

    public abstract long onResultLocked(Transaction.Status result);

    public abstract void onResultUnlocked();

    // Future

    public final boolean cancel(boolean flag) {
        throw new UnsupportedOperationException();
    }

    public final Status get() throws InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    public final Status get(long l, TimeUnit timeunit) throws InterruptedException, ExecutionException, TimeoutException {
        _sync = new Object();

        synchronized (_sync) {
            while (!isDone())
                _sync.wait(timeunit.toMillis(l));
        }

        return _transaction.getStatus();
    }

    public final boolean isCancelled() {
        return false;
    }

    public final boolean isDone() {
        return _transaction.getStatus() != Transaction.Status.SUSPENDED;
    }
}