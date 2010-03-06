/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver;

import java.io.IOException;
import java.util.concurrent.*;

import jstm.core.MethodCallback;
import jstm.core.Site;

public abstract class Client extends ClientBase {

    private volatile ConnectFuture _connectFuture;

    public Client(Site localSite) {
        super(localSite);
    }

    @SuppressWarnings("unused")
    public final ConnectionInfo connect() throws IOException {
        Future<ConnectionInfo> future = beginConnect(null);

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }

        return null;
    }

    @SuppressWarnings("unused")
    public final Future<ConnectionInfo> beginConnect(MethodCallback<ConnectionInfo> callback) throws IOException {
        ConnectFuture future = _connectFuture;

        if (future != null)
            throw new IllegalStateException("Already connecting");

        setStatus(Status.CONNECTING);
        createReaderAndWriter();
        setStatus(Status.SYNCHRONIZING);
        requestInitialObjects();

        future = _connectFuture = new ConnectFuture(callback);
        return future;
    }

    @Override
    protected final MethodCallback<ConnectionInfo> onFirstCommit(ConnectionInfo info) {
        ConnectFuture future = _connectFuture;

        if (future != null) {
            MethodCallback<ConnectionInfo> callback = future._callback;
            future.setConnectionInfo(info);
            _connectFuture = null;
            return callback;
        }

        return null;
    }

    @Override
    protected void _dispose(IOException ex) {
        super._dispose(ex);

        ConnectFuture future = _connectFuture;

        if (future != null)
            future.cancel(true);

        _connectFuture = null;
    }

    private final class ConnectFuture implements Future<ConnectionInfo> {

        protected final MethodCallback<ConnectionInfo> _callback;

        private ConnectionInfo _info;

        private boolean _cancelled;

        private final Object _lock = new Object();

        protected ConnectFuture(MethodCallback<ConnectionInfo> callback) {
            _callback = callback;
        }

        public boolean cancel(boolean flag) {
            boolean result;

            synchronized (_lock) {
                result = _cancelled;
                _cancelled = true;
                _lock.notifyAll();
            }

            return result;
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
            synchronized (_lock) {
                while (!isDone())
                    _lock.wait(timeUnit.toMillis(timeSpan));
            }

            if (isCancelled())
                throw new CancellationException("Connection has been cancelled.");

            return _info;
        }

        public boolean isCancelled() {
            synchronized (_lock) {
                return _cancelled;
            }
        }

        public boolean isDone() {
            synchronized (_lock) {
                return _info != null || _cancelled;
            }
        }

        protected void setConnectionInfo(ConnectionInfo info) {
            synchronized (_lock) {
                _info = info;
                _lock.notifyAll();
            }
        }
    }
}
