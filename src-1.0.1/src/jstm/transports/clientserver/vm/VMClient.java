/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.vm;

import java.io.IOException;

import jstm.core.Site;
import jstm.misc.BoundedBuffer;
import jstm.misc.Debug;
import jstm.misc.BinaryReader.Filler;
import jstm.misc.BinaryWriter.Flusher;
import jstm.transports.clientserver.Client;

public final class VMClient extends Client {

    protected final BoundedBuffer.Flusher Flusher = new BoundedBuffer.Flusher();

    private final VMSession _session;

    protected final int _number;

    protected VMClient(VMSession session, Site localSite, int number) {
        super(localSite);

        _session = session;
        _number = number;

        allowThread(localSite);

        Debug.assertion(getLocalSite() != _session.getServerSite());
    }

    @Override
    protected Filler createFiller() {
        return new BoundedBuffer.Filler(_session.Flusher.Buffer);
    }

    @Override
    protected Flusher createFlusher() {
        return Flusher;
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZING) {
            Thread thread = new Thread(this + " Reader") {

                @Override
                public void run() {
                    allowThread(getLocalSite());

                    while (true) {
                        try {
                            getReader().readMessage();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            };

            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public String toString() {
        return "Client " + _number;
    }
}
