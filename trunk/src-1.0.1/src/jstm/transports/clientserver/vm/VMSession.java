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
import jstm.transports.clientserver.Session;

final class VMSession extends Session {

    protected final BoundedBuffer.Flusher Flusher = new BoundedBuffer.Flusher();

    protected VMClient Client;

    private volatile Thread _thread;

    private volatile boolean _run;

    public VMSession(VMServer server) {
        super(server);

        allowThread(Site.getLocal());
    }

    protected Site getServerSite() {
        Site site = getLocalSite();
        Debug.assertion(site == ((VMServer) getServer()).getSite());
        return site;
    }

    @Override
    protected Filler createFiller() {
        return new BoundedBuffer.Filler(Client.Flusher.Buffer);
    }

    @Override
    protected Flusher createFlusher() {
        return Flusher;
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZING) {
            _thread = new Thread(VMSession.this + " Reader") {

                @Override
                public void run() {
                    allowThread(Site.getLocal());

                    _run = true;

                    while (_run) {
                        try {
                            getReader().readMessage();
                        } catch (IOException ex) {
                            Debug.assertion(!_run);
                        }
                    }
                }
            };

            _thread.setDaemon(true);
            _thread.start();
        }
    }

    @Override
    protected void _dispose(IOException ex) {
        // Unblock writer thread if needed

        BoundedBuffer.Flusher flusher = Client.Flusher;
        flusher.Buffer.clear();

        // Stop reading

        _run = false;

        Thread thread = _thread;

        if (thread != null) {
            thread.interrupt();

            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }

        super._dispose(ex);
    }

    @Override
    public String toString() {
        return "Session " + Client._number;
    }
}
