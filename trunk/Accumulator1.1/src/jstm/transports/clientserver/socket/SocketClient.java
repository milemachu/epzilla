/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.transports.clientserver.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jstm.core.Site;
import jstm.misc.Debug;
import jstm.misc.StreamFiller;
import jstm.misc.StreamFlusher;
import jstm.misc.BinaryReader.Filler;
import jstm.misc.BinaryWriter.Flusher;
import jstm.transports.clientserver.Client;

public final class SocketClient extends Client {

    private final String _host;

    private final int _port;

    private Socket _socket;

    public SocketClient(String host, int port) {
        this(Site.getLocal(), host, port);
    }

    protected SocketClient(Site local, String host, int port) {
        super(local);

        _host = host;
        _port = port;

        allowThread(getLocalSite());
    }

    @Override
    protected Filler createFiller() throws IOException {
        if (_socket == null)
            _socket = new Socket(_host, _port);

        InputStream stream = _socket.getInputStream();
        return new StreamFiller(stream);
    }

    @Override
    protected Flusher createFlusher() throws IOException {
        if (_socket == null)
            _socket = new Socket(_host, _port);

        OutputStream stream = _socket.getOutputStream();
        return new StreamFlusher(stream);
    }

    @Override
    protected void onStatusChanged(Status value) {
        super.onStatusChanged(value);

        if (value == Status.SYNCHRONIZING) {
            Thread thread = new ReaderThread();
            thread.start();
        }
    }

    public void disconnect() {
        try {
            if (_socket != null)
                _socket.close();
        } catch (IOException e) {
            // Ignore
        }
    }

    private final class ReaderThread extends Thread {

        public ReaderThread() {
            super("Client Socket Reader");

            setDaemon(true);
        }

        @Override
        public void run() {
            allowThread(getLocalSite());

            IOException ex;

            try {

                while (true) {
                    getReader().readMessage();
                }
            } catch (IOException e) {
                ex = e;
            }

            dispose(ex);

            if (Debug.MESSAGING)
                Debug.log("Disconnected (" + (ex.getCause() != null ? ex.getCause() : ex) + ")");
        }
    }
}
