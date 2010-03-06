/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.polling;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import jstm.core.Site;
import jstm.misc.BinaryWriter;
import jstm.misc.Debug;
import jstm.misc.ThreadPool;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.Session;

public class PollingServer extends Server {

    private final AtomicLong _sessionId = new AtomicLong(0);

    private final ThreadPool.Task _deadSessionsRemover;

    public PollingServer() {
        super(Site.getLocal());

        _deadSessionsRemover = ThreadPool.scheduleAtFixedRate(new DeadSessionRemover(), 1000);
    }

    @Override
    protected void listen() throws IOException {
    }

    @Override
    public void _dispose(IOException ex) {
        _deadSessionsRemover.cancel();

        super._dispose(ex);
    }

    protected final String addSession(PollingSession session, boolean writeId) throws IOException {
        BinaryWriter writer = session.connectAndGetWriter();

        String id = Long.toString(_sessionId.getAndIncrement());

        if (writeId) {
            writer.writeString(id);
            writer.flush();
        }

        onConnection(id, session);

        return id;
    }

    private final class DeadSessionRemover implements Runnable {

        public void run() {
            try {
                for (Map.Entry<Object, Session> entry : getSessions().entrySet()) {
                    if (((PollingSession) entry.getValue()).isDead()) {
                        Debug.log(1, "Session timeout");
                        allowThread(Site.getLocal());
                        onDisconnection(entry.getKey());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
