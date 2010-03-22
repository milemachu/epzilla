/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.polling;

import java.io.IOException;

import jstm.core.Writer;
import jstm.transports.clientserver.Session;

public abstract class PollingSession extends Session {

    public static final int SESSION_TIMEOUT_AS_SECONDS = 10;

    private long _lastCall;

    protected PollingSession(PollingServer server) {
        super(server);

        notifyOfCall();
    }

    public final boolean isDead() {
        return System.nanoTime() - _lastCall > SESSION_TIMEOUT_AS_SECONDS * (long) 1e9;
    }

    public final void notifyOfCall() {
        _lastCall = System.nanoTime();
    }

    protected final Writer connectAndGetWriter() throws IOException {
        setStatus(Status.CONNECTING);
        createReaderAndWriter();
        return super.getWriter();
    }

    @Override
    protected void start() throws IOException {
        setStatus(Status.SYNCHRONIZING);
        requestInitialObjects();
    }
}
